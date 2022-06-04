package de.dietrichpaul.winkel.injection.mixin.client;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (client.player != null && client.currentScreen == null) {
            Zoom zoom = WinkelClient.INSTANCE.getZoom();
            zoom.updateZoomingState();
            if (zoom.isZooming()) {
                zoom.scroll(vertical);
                ci.cancel();
            }
        }
    }

}
