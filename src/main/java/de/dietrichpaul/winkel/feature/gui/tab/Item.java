package de.dietrichpaul.winkel.feature.gui.tab;

import net.minecraft.client.util.math.MatrixStack;

public abstract class Item {

    protected int x;
    protected int y;
    protected int maxWidth;

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract void render(MatrixStack matrices, float delta);

    public abstract boolean onKey(int key);

    public int getX() {
        return x;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getY() {
        return y;
    }

}
