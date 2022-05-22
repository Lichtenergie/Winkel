package de.dietrichpaul.winkel.injection.mixin.client;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.KeyInputListener;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        WinkelClient.INSTANCE.getEventDispatcher().post(new KeyInputListener.KeyInputEvent(key, scancode, action, modifiers));
    }

}
