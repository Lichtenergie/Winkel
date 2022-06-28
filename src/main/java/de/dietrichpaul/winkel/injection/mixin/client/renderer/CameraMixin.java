package de.dietrichpaul.winkel.injection.mixin.client.renderer;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.CameraTransformRotationListener;
import de.dietrichpaul.winkel.injection.accessor.client.renderer.ICameraMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin implements ICameraMixin {

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Unique
    private MinecraftClient client;

    @Unique
    private float prevTickDelta;

    @Inject(method = "update", at = @At("HEAD"))
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        this.prevTickDelta = tickDelta;
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0))
    public void setRotation(Camera instance, float yaw, float pitch) {
        CameraTransformRotationListener.CameraTransformRotationEvent event
                = new CameraTransformRotationListener.CameraTransformRotationEvent(yaw, pitch, this.prevTickDelta);
        WinkelClient.INSTANCE.getEventDispatcher().post(event);
        ((ICameraMixin) instance).accessRotation(event.getInterpolatedYaw(), event.getInterpolatedPitch());
    }

    @Override
    public void setClient(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void accessRotation(float yaw, float pitch) {
        this.setRotation(yaw, pitch);
    }

}
