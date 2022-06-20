package de.dietrichpaul.winkel.injection.mixin.client.network;

import com.mojang.authlib.GameProfile;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.FreezeRotationUpdateListener;
import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.event.list.network.MovementPacketListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.ForceSpoofRotationListener;
import de.dietrichpaul.winkel.event.list.tick.move.StrafeListener;
import de.dietrichpaul.winkel.feature.hack.impl.movement.SprintHack;
import de.dietrichpaul.winkel.injection.accessor.client.network.IClientPlayerEntityMixin;
import de.dietrichpaul.winkel.injection.accessor.entity.IEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements IClientPlayerEntityMixin {

    @Shadow public float lastRenderYaw;

    @Shadow public float lastRenderPitch;

    @Shadow private boolean lastSprinting;

    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Shadow private boolean lastSneaking;

    @Shadow private double lastX;

    @Shadow private double lastBaseY;

    @Shadow private double lastZ;

    @Shadow protected abstract boolean isCamera();

    @Shadow private float lastYaw;

    @Shadow private float lastPitch;

    @Shadow private int ticksSinceLastPositionPacketSent;

    @Shadow private boolean lastOnGround;

    @Shadow private boolean autoJumpEnabled;

    @Shadow @Final protected MinecraftClient client;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    /**
     * @author Paul Dietrich, Mojang Studios
     * @reason flying packets
     */
    @Overwrite
    private void sendMovementPackets() {
        boolean prevSneak = this.isSneaking();
        boolean prevSprint = this.isSprinting();
        if (prevSprint != this.lastSprinting) {
            ClientCommandC2SPacket.Mode mode = prevSprint ? ClientCommandC2SPacket.Mode.START_SPRINTING : ClientCommandC2SPacket.Mode.STOP_SPRINTING;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
            this.lastSprinting = prevSprint;
        }
        if (prevSneak != this.lastSneaking) {
            ClientCommandC2SPacket.Mode mode2 = prevSneak ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode2));
            this.lastSneaking = prevSneak;
        }
        if (this.isCamera()) {
            MovementPacketListener.MovementPacketEvent event = new MovementPacketListener.MovementPacketEvent(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch(), this.onGround);
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            double deltaX = event.getX() - this.lastX;
            double deltaY = event.getY() - this.lastBaseY;
            double deltaZ = event.getZ() - this.lastZ;
            double deltaYaw = event.getYaw() - this.lastYaw;
            double deltaPitch = event.getPitch() - this.lastPitch;
            ++this.ticksSinceLastPositionPacketSent;
            boolean updatePosition = MathHelper.squaredMagnitude(deltaX, deltaY, deltaZ) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20;
            boolean updateRotation = deltaYaw != 0.0 || deltaPitch != 0.0;
            if (this.hasVehicle()) {
                Vec3d velocity = this.getVelocity();
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(velocity.x, -999.0, velocity.z, event.getYaw(), event.getPitch(), event.isOnGround()));
                updatePosition = false;
            } else if (updatePosition && updateRotation) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (updatePosition) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(event.getX(), event.getY(), event.getZ(), event.isOnGround()));
            } else if (updateRotation) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (this.lastOnGround != this.onGround) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(event.isOnGround()));
            }
            if (updatePosition) {
                this.lastX = event.getX();
                this.lastBaseY = event.getY();
                this.lastZ = event.getZ();
                this.ticksSinceLastPositionPacketSent = 0;
            }
            if (updateRotation) {
                this.lastYaw = event.getYaw();
                this.lastPitch = event.getPitch();
            }
            this.lastOnGround = event.isOnGround();
            this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
        }
    }

    @Override
    public void updateVelocity(float speed, Vec3d movementInput) {
        StrafeListener.StrafeEvent event = new StrafeListener.StrafeEvent(this.getYaw());
        WinkelClient.INSTANCE.getEventDispatcher().post(event);
        Vec3d vec3d = ((IEntityMixin) this).accessMovementInputToVelocity(movementInput, speed, event.getYaw());
        this.setVelocity(this.getVelocity().add(vec3d));
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
        super.setRotation(yaw, pitch);
        ForceSpoofRotationListener.ForceSpoofRotationEvent event = new ForceSpoofRotationListener.ForceSpoofRotationEvent(yaw % 360.0f, pitch % 360.0f, true, true);
        WinkelClient.INSTANCE.getEventDispatcher().post(event);
    }

    @Override
    public void updatePositionAndAngles(double x, double y, double z, float yaw, float pitch) {
        super.updatePositionAndAngles(x, y, z, yaw, pitch);
        WinkelClient.INSTANCE.getEventDispatcher()
                .post(new ForceSpoofRotationListener.ForceSpoofRotationEvent(this.getYaw(), this.getPitch(), true, true));
    }

    @Override
    public void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
        FreezeRotationUpdateListener.FreezeRotationUpdateEvent event = new FreezeRotationUpdateListener.FreezeRotationUpdateEvent();
        WinkelClient.INSTANCE.getEventDispatcher().post(event);

        if (!event.isFrozen())
            super.changeLookDirection(cursorDeltaX, cursorDeltaY);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.BEFORE))
    public void onTick(CallbackInfo ci) {
        WinkelClient.INSTANCE.getEventDispatcher().post(UpdateListener.UpdateEvent.INSTANCE);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    public boolean onValidateSprintForward(Input instance) {
        SprintHack sprint = WinkelClient.INSTANCE.getHackList().sprint;
        if (sprint.isEnabled() && sprint.isInAllDirection()) {
            return instance.movementForward != 0 || instance.movementSideways != 0;
        }
        return instance.hasForwardMovement();
    }

    @Override
    public boolean hasStatusEffect(StatusEffect effect) {
        if (effect == StatusEffects.NIGHT_VISION && WinkelClient.INSTANCE.getHackList().fullBright.isEnabled())
            return true;
        return super.hasStatusEffect(effect);
    }

    @Override
    public float getLastReportedYaw() {
        return this.lastYaw;
    }

    @Override
    public float getLastReportedPitch() {
        return this.lastPitch;
    }

}
