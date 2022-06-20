package de.dietrichpaul.winkel.injection.mixin.entity;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.rotationrequest.ForceSpoofRotationListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedPitchListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedYawListener;
import de.dietrichpaul.winkel.event.list.tick.move.JumpStrafeListener;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected abstract void initDataTracker();

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    public float requestYaw0(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            RequestSpoofedYawListener.RequestSpoofedYawEvent event = new RequestSpoofedYawListener.RequestSpoofedYawEvent(instance.getYaw());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getYaw();
        }
        return instance.getYaw();
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
    public float requestForPrevPitch(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            RequestSpoofedPitchListener.RequestSpoofedPitchEvent event = new RequestSpoofedPitchListener.RequestSpoofedPitchEvent(instance.getPitch());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getPitch();
        }
        return instance.getPitch();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
    public float requestPitch(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            RequestSpoofedPitchListener.RequestSpoofedPitchEvent event = new RequestSpoofedPitchListener.RequestSpoofedPitchEvent(instance.getPitch());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getPitch();
        }
        return instance.getPitch();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setYaw(F)V", ordinal = 0))
    public void forceUnwrappedYaw(LivingEntity instance, float v) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            ForceSpoofRotationListener.ForceSpoofRotationEvent event = new ForceSpoofRotationListener.ForceSpoofRotationEvent(v, 0, true, false);
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
        }
        instance.setYaw(v);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setPitch(F)V", ordinal = 0))
    public void forceUnwrappedPitch(LivingEntity instance, float v) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            ForceSpoofRotationListener.ForceSpoofRotationEvent event = new ForceSpoofRotationListener.ForceSpoofRotationEvent(0, v, false, true);
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
        }
        instance.setPitch(v);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    public float redirectHeadYaw(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            RequestSpoofedYawListener.RequestSpoofedYawEvent event = new RequestSpoofedYawListener.RequestSpoofedYawEvent(instance.getYaw());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getYaw();
        }
        return instance.getYaw();
    }

    @Redirect(method = "turnHead", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    public float redirectBodyYaw(LivingEntity instance, float bodyRotation, float headRotation) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            RequestSpoofedYawListener.RequestSpoofedYawEvent event = new RequestSpoofedYawListener.RequestSpoofedYawEvent(instance.getYaw());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getYaw();
        }
        return instance.getYaw();
    }

    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    public float getJumpDirection(LivingEntity instance) {
        JumpStrafeListener.JumpStrafeEvent event = new JumpStrafeListener.JumpStrafeEvent(instance.getYaw());
        WinkelClient.INSTANCE.getEventDispatcher().post(event);
        return event.getYaw();
    }

}
