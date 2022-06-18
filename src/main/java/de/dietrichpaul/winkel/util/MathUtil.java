package de.dietrichpaul.winkel.util;

import net.minecraft.util.math.MathHelper;

public class MathUtil {

    public static int conjugate(int value) {
        if (value == Integer.MIN_VALUE)
            return Integer.MAX_VALUE;
        if (value == Integer.MAX_VALUE)
            return Integer.MIN_VALUE;
        return -value;
    }

    public static float setPrecision(double x, int precision) {
        float scalar = (int) Math.pow(10, precision);
        return ((float) (int) Math.round(x * scalar)) / scalar;
    }

    public static double limitChange(double prev, double next, double speed) {
        return prev + MathHelper.clamp(next - prev, -speed, speed);
    }

}
