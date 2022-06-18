package de.dietrichpaul.winkel.injection.mixin.client.renderer;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.Zoom;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow public abstract void tick();

    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionStrength(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (WinkelClient.INSTANCE.getHackList().fullBright.isEnabled())
            cir.setReturnValue(WinkelClient.INSTANCE.getHackList().fullBright.getStrength());
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        Zoom zoom = WinkelClient.INSTANCE.getZoom();
        zoom.updateZoomingState();
        cir.setReturnValue(zoom.getFOV(tickDelta, cir.getReturnValue()));
    }


}
