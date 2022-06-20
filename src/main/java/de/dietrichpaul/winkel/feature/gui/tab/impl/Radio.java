package de.dietrichpaul.winkel.feature.gui.tab.impl;

import de.dietrichpaul.winkel.feature.gui.tab.Item;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.function.Supplier;

public class Radio extends Item {

    private Supplier<Text> displaySupplier;
    private Supplier<Boolean> stateSupplier;
    private Runnable clickAction;

    public Radio(Supplier<Text> displaySupplier, Supplier<Boolean> stateSupplier, Runnable clickAction) {
        this.displaySupplier = displaySupplier;
        this.stateSupplier = stateSupplier;
        this.clickAction = clickAction;
    }

    @Override
    public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 4;
    }

    @Override
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(this.getText()) + 4;
    }

    private Text getText() {
        Text text = this.displaySupplier.get();
        MutableText prefix = null;

        if (this.stateSupplier.get()) {
            prefix = Text.literal("● ");
        } else {
            prefix = Text.literal("○ ");
        }

        return prefix.append(text);
    }

    @Override
    public void render(MatrixStack matrices, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.drawWithShadow(matrices, this.getText(), this.x + 2, this.y + 2F, -1);
    }

    @Override
    public boolean onKey(int key) {
        if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            this.clickAction.run();
            return true;
        }
        return false;
    }

}
