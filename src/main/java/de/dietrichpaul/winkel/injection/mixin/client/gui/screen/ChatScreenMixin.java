package de.dietrichpaul.winkel.injection.mixin.client.gui.screen;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.ChatListener;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

    public ChatScreenMixin(Text title) {
        super(title);
    }

    @Override
    public void sendMessage(String message) {
        ChatListener.ChatEvent event =
                new ChatListener.ChatEvent(message, client.inGameHud.getChatHud());
        WinkelClient.INSTANCE.getEventDispatcher().post(event);
        if (!event.isCancelled())
            super.sendMessage(message);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void renderHead(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrices.push();
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderTail(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrices.pop();
    }

}