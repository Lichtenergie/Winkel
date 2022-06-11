package de.dietrichpaul.winkel.feature.gui.tab.impl;

import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Scroll extends Item {

    private Supplier<Float> valueSupplier;
    private Supplier<Float> incrementSupplier;
    private Supplier<Float> minSupplier;
    private Supplier<Float> maxSupplier;
    private Supplier<Integer> decimalPrecisionSupplier;
    private Consumer<Float> valueApplier;

    private DecimalFormat floatFormat;
    private int formatPrecision = -1;

    public Scroll(Supplier<Float> valueSupplier, Supplier<Float> incrementSupplier, Supplier<Float> minSupplier, Supplier<Float> maxSupplier, Supplier<Integer> decimalPrecisionSupplier, Consumer<Float> valueApplier) {
        this.valueSupplier = valueSupplier;
        this.incrementSupplier = incrementSupplier;
        this.minSupplier = minSupplier;
        this.maxSupplier = maxSupplier;
        this.decimalPrecisionSupplier = decimalPrecisionSupplier;
        this.valueApplier = valueApplier;
    }

    private Text getText() {
        int precision = this.decimalPrecisionSupplier.get();
        if (precision != this.formatPrecision) {
            this.floatFormat = new DecimalFormat("0." + "0".repeat(precision));
            this.formatPrecision = precision;
        }
        float value = this.valueSupplier.get();
        float min = this.minSupplier.get();
        float max = this.maxSupplier.get();

        MutableText valueText = Text.literal(floatFormat.format(value));

        MutableText extraMax = Text.literal(" ⬆");
        if (max == Float.POSITIVE_INFINITY) {
            extraMax.formatted(Formatting.DARK_GREEN);
        } else if (value == max) {
            extraMax.formatted(Formatting.RED);
        } else {
            extraMax.formatted(Formatting.GREEN);
        }

        MutableText extraMin = Text.literal("⬇");
        if (min == Float.NEGATIVE_INFINITY) {
            extraMin.formatted(Formatting.DARK_GREEN);
        } else if (value == min) {
            extraMin.formatted(Formatting.RED);
        } else {
            extraMin.formatted(Formatting.GREEN);
        }

        return valueText.append(extraMax).append(extraMin);
    }

    @Override
    public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 4;
    }

    @Override
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(this.getText()) + 4;
    }

    @Override
    public void render(MatrixStack matrices, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.drawWithShadow(matrices, this.getText(), this.x + 2, this.y + 2F, -1);
    }

    @Override
    public boolean onKey(int key) {
        if (key != GLFW.GLFW_KEY_DOWN && key != GLFW.GLFW_KEY_UP)
            return false;
        int direction = key == GLFW.GLFW_KEY_DOWN ? -1 : 1;
        float value = this.valueSupplier.get();
        float increment = this.incrementSupplier.get();
        float min = this.minSupplier.get();
        float max = this.maxSupplier.get();
        int precision = this.decimalPrecisionSupplier.get();
        System.out.println(precision);
        this.valueApplier.accept(MathUtil.setPrecision(MathHelper.clamp(value + increment * direction, min, max), precision));
        return true;
    }

}
