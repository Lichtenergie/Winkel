package de.dietrichpaul.winkel.feature.gui.tab.impl;

import de.dietrichpaul.winkel.feature.gui.tab.Item;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class Container extends Item {

    private Supplier<Text> textSupplier;

    private boolean expanded;

    private List<Item> items = new LinkedList<>();

    private int width;
    private int selection;

    public Container(Supplier<Text> textSupplier) {
        this.textSupplier = textSupplier;
    }

    public void add(Item item) {
        this.items.add(item);
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

        if (this.expanded) {
            this.updatePositions(this.x + this.maxWidth + 4, this.y);
            List<Item> itemList = this.items;
            for (int i = 0; i < itemList.size(); i++) {
                Item item = itemList.get(i);
                DrawableHelper.fill(matrices, item.getX(), item.getY(), item.getX() + item.getMaxWidth(), item.getY() + item.getHeight(), i == this.selection ? 0xff9a9a2a : 0xff2a2a2a);
                item.render(matrices, delta);
            }
        }
    }

    public void updatePositions(int x, int y) {
        int height = 0;
        this.width = 0;
        for (Item item : this.items) {
            item.updatePosition(x, y + height);
            height += item.getHeight();
            this.width = Math.max(this.width, item.getWidth());
        }
        for (Item item : this.items) {
            item.setMaxWidth(this.width);
        }
    }

    @Override
    public boolean onKey(int key) {
        if (this.expanded) {
            if (!this.items.get(this.selection).onKey(key)) {
                switch (key) {

                    case GLFW.GLFW_KEY_UP:
                        selection--;
                        break;


                    case GLFW.GLFW_KEY_DOWN:
                        selection++;
                        break;

                }
            }
            if (selection == -1)
                selection = this.items.size() - 1;
            if (selection == this.items.size()) {
                selection = 0;
            }
        }
        boolean prevExpanded = this.expanded;
        if (key == GLFW.GLFW_KEY_RIGHT && !this.items.isEmpty()) {
            this.expanded = true;
        } else if (key == GLFW.GLFW_KEY_LEFT) {
            this.expanded = false;
        }
        return prevExpanded || this.expanded;
    }

}
