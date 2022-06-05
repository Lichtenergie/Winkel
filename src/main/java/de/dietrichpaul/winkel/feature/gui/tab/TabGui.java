package de.dietrichpaul.winkel.feature.gui.tab;

import de.dietrichpaul.winkel.event.list.KeyInputListener;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TabGui implements KeyInputListener {

    private List<Item> items = new LinkedList<>();

    private int width;
    private int selection;

    public void add(Item item) {
        this.items.add(item);
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

    public void tick() {

    }

    public void render(MatrixStack matrices, float delta) {
        this.updatePositions(2, 36);
        List<Item> itemList = this.items;
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            DrawableHelper.fill(matrices, item.x, item.y, item.x + item.maxWidth, item.y + item.getHeight(), i == this.selection ? 0xff9a9a2a : 0xff2a2a2a);
            item.render(matrices, delta);
        }
    }

    @Override
    public void onInput(KeyInputEvent event) {
        if ((event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_REPEAT) && !this.items.get(this.selection).onKey(event.getKey())) {
            switch (event.getKey()) {

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

}
