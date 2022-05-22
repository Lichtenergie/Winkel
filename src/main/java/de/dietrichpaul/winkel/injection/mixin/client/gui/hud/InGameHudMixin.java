package de.dietrichpaul.winkel.injection.mixin.client.gui.hud;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.render.RenderOverlayListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void onPostRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        WinkelClient.INSTANCE.getEventDispatcher().post(new RenderOverlayListener.RenderOverlayEvent(tickDelta, matrices));
    }

}
