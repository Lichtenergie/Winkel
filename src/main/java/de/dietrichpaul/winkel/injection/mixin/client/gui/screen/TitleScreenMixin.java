package de.dietrichpaul.winkel.injection.mixin.client.gui.screen;

import de.dietrichpaul.winkel.ui.screen.list.WinkelMainMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("HEAD"))
    public void onInit(CallbackInfo ci) {
        MinecraftClient.getInstance().setScreen(new WinkelMainMenu());
    }

}
