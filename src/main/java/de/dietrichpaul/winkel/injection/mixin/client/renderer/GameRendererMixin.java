package de.dietrichpaul.winkel.injection.mixin.client.renderer;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.Zoom;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionStrength(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (WinkelClient.INSTANCE.getHackList().fullBright.isEnabled())
            cir.setReturnValue(WinkelClient.INSTANCE.getHackList().fullBright.getStrength());
    }

    @Redirect(method = "getFov", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;fov:D"))
    public double onGetFOV(GameOptions instance) {
        Zoom zoom = WinkelClient.INSTANCE.getZoom();
        zoom.updateZoomingState();
        if (!zoom.isZooming())
            return instance.fov;
        return zoom.getFOV(instance.fov);
    }


}
