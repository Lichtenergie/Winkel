package de.dietrichpaul.winkel.feature.gui.tab.impl;

import de.dietrichpaul.winkel.feature.gui.tab.Item;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class Button extends Item {

    private Supplier<Text> textSupplier;
    private Runnable clickAction;

    public Button(Supplier<Text> textSupplier, Runnable clickAction) {
        this.textSupplier = textSupplier;
        this.clickAction = clickAction;
    }

    @Override
    public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 4;
    }

    @Override
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(this.textSupplier.get()) + 4;
    }

    @Override
    public void render(MatrixStack matrices, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.drawWithShadow(matrices, this.textSupplier.get(), this.x + 2, this.y + 2F, -1);
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
