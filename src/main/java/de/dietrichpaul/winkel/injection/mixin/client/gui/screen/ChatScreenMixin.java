package de.dietrichpaul.winkel.injection.mixin.client.gui.screen;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.ChatListener;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ChatPreviewer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow public abstract String normalize(String chatText);

    @Shadow private String originalChatText;

    @Shadow private ChatPreviewer chatPreviewer;

    public ChatScreenMixin(Text title) {
        super(title);
    }

    /**
     * @author Mojang Studios, Cach30verfl0w
     * @reason Inject chat event into send message method from minecraft.
     */
    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfo callback) {
        assert client != null;
        ChatListener.ChatEvent event = new ChatListener.ChatEvent(chatText, client.inGameHud.getChatHud());
        WinkelClient.INSTANCE.getEventDispatcher().post(event);
        if (event.isCancelled())
            callback.cancel();
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