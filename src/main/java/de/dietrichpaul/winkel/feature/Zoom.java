package de.dietrichpaul.winkel.feature;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.GameTickListener;
import de.dietrichpaul.winkel.util.MathUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class Zoom implements GameTickListener {

    public static final double RESET_DIVISOR = 3;
    private final KeyBinding keyBinding;
    private boolean zooming;
    private boolean prevZooming;
    private double targetDivisor = RESET_DIVISOR;
    private double divisor = targetDivisor;
    private double prevDivisor;

    public Zoom() {
        this.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.winkel.zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.winkel"
        ));
        WinkelClient.INSTANCE.getEventDispatcher().subscribe(GameTickListener.class, this);
    }

    public double getFOV(float partials, double prev) {
        double interpolatedDivisor = MathHelper.lerp(partials, this.prevDivisor, this.divisor);
        return prev / interpolatedDivisor;
    }

    public void scroll(double offset) {
        this.targetDivisor = MathHelper.clamp(this.targetDivisor + offset * 0.25, 1, 30);
    }

    public boolean isZooming() {
        return zooming;
    }

    public void updateZoomingState() {
        this.zooming = this.keyBinding.isPressed();
        if (this.zooming && !this.prevZooming) {
            this.targetDivisor = RESET_DIVISOR;
        }
        this.prevZooming = this.zooming;
    }

    @Override
    public void onTick() {
        if (!isZooming()) {
            targetDivisor = 1;
        }
        this.prevDivisor = this.divisor;
        if (targetDivisor > this.divisor && Math.abs(this.targetDivisor - this.divisor) > 0.75) {
            this.divisor = MathUtil.limitChange(this.divisor, this.targetDivisor, 0.75);
        } else {
            this.divisor = MathHelper.lerp(0.75, this.divisor, this.targetDivisor);
        }
    }

}
