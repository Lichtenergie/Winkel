package de.dietrichpaul.winkel.feature;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class Zoom {

    public static final double RESET_DIVISOR = 3;

    private boolean zooming;
    private boolean prevZooming;

    private double divisor = RESET_DIVISOR;

    private final KeyBinding keyBinding;

    public Zoom() {
        this.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.winkel.zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.winkel"
        ));
    }

    public double getFOV(double prev) {
        return prev / divisor;
    }

    public void scroll(double offset) {
        this.divisor = MathHelper.clamp(this.divisor + offset * 0.25, 1, 50);
    }

    public boolean isZooming() {
        return zooming;
    }

    public void updateZoomingState() {
        this.zooming = this.keyBinding.isPressed();
        if (this.zooming && !this.prevZooming) {
            this.divisor = RESET_DIVISOR;
        }
        this.prevZooming = this.zooming;
    }

}
