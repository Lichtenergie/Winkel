package de.dietrichpaul.winkel.ui.screen.widget;

import de.dietrichpaul.winkel.WinkelClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class CenteredButtonList implements Drawable, Element, Selectable {

    private List<Entry> list = new LinkedList<>();

    private int width;
    private int x;
    private int centerY;

    public CenteredButtonList(int x, int width, int centerY) {
        this.x = x;
        this.width = width;
        this.centerY = centerY;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        MinecraftClient client = MinecraftClient.getInstance();
        int height = (this.list.size()) * (client.textRenderer.fontHeight + 12);
        if (mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.centerY - height / 2 - 6 && mouseY < this.centerY + height / 2 + 6) {
            // y = centerY - height / 2 + i * (client.textRenderer.fontHeight + 12) | -centerY
            // y - centerY = -(height / 2) + i * (client.textRenderer.fontHeight + 12) | + (height / 2)
            // y - centerY + (height / 2) = i * (client.textRenderer.fontHeight + 12) | /(client.textRenderer.fontHeight + 12)
            // (y - centerY + (height / 2)) / (client.textRenderer.fontHeight + 12) = i
            int i = MathHelper.floor((mouseY - centerY + (height / 2)) / (client.textRenderer.fontHeight + 12));
            Entry entry = this.list.get(i);
            if (entry.clickAction != null)
                entry.clickAction.run();
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int height = (this.list.size()) * (client.textRenderer.fontHeight + 12);
        if (WinkelClient.DEBUG_GUI)
            DrawableHelper.fill(matrices, x, centerY - height / 2, x + width, centerY + height / 2, 0x800000ff);
        for (int i = 0; i < list.size(); i++) {
            int y = centerY - height / 2 + i * (client.textRenderer.fontHeight + 12) + 6;
            boolean hovered = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= y - 6 && mouseY < y + client.textRenderer.fontHeight + 6;
            Entry entry = this.list.get(i);
            MutableText text = Text.literal("");
            text.append(entry.display);
            if (hovered && entry.hoverAction != null) {
                entry.hoverAction.run();
            }
            if (!hovered && !(entry.stateSupplier != null && entry.stateSupplier.getAsBoolean()))
                text.formatted(Formatting.GRAY);

            client.textRenderer.drawWithShadow(matrices, text, x + 4, y, -1);
        }
    }

    public void addButton(Entry entry) {
        this.list.add(entry);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    public static class Entry {

        private final Text display;
        private final Runnable hoverAction;
        private final Runnable clickAction;
        private final BooleanSupplier stateSupplier;

        public Entry(Text display, Runnable hoverAction, Runnable clickAction, BooleanSupplier stateSupplier) {
            this.display = display;
            this.hoverAction = hoverAction;
            this.clickAction = clickAction;
            this.stateSupplier = stateSupplier;
        }

    }

}
