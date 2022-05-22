package de.dietrichpaul.winkel.injection.mixin.client.gui.screen;

import de.dietrichpaul.winkel.feature.command.CustomCommandSuggestor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandSuggestor.class)
public abstract class CommandSuggestorMixin {

    @Unique
    private CustomCommandSuggestor customCommandSuggestor;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    public void injectConstructorReturn(MinecraftClient client, Screen owner, TextFieldWidget textField,
                                        TextRenderer textRenderer, boolean slashRequired, boolean suggestingWhenEmpty,
                                        int inWindowIndexOffset, int maxSuggestionSize, boolean chatScreenSized,
                                        int color, CallbackInfo ci) {
        this.customCommandSuggestor = new CustomCommandSuggestor(client, owner, textField, textRenderer, slashRequired,
                suggestingWhenEmpty, inWindowIndexOffset, maxSuggestionSize, chatScreenSized, color);
    }

    /**
     * @author AdvancedCode
     * @reason implementing own commands.
     */
    @Overwrite
    public void refresh() {
        this.customCommandSuggestor.refresh();
    }

    /**
     * @author AdvancedCode
     * @reason implementing own commands.
     */
    @Overwrite
    public void setWindowActive(boolean windowActive) {
        this.customCommandSuggestor.setWindowActive(windowActive);
    }

    /**
     * @author AdvancedCode
     * @reason implementing own commands.
     */
    @Overwrite
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.customCommandSuggestor.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * @author AdvancedCode
     * @reason implementing own commands.
     */
    @Overwrite
    public boolean mouseScrolled(double amount) {
        return this.customCommandSuggestor.mouseScrolled(amount);
    }

    /**
     * @author AdvancedCode
     * @reason implementing own commands.
     */
    @Overwrite
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.customCommandSuggestor.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * @author AdvancedCode
     * @reason implementing own commands.
     */
    @Overwrite
    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        this.customCommandSuggestor.render(matrices, mouseX, mouseY);
    }

}
