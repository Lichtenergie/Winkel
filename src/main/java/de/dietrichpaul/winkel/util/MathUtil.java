package de.dietrichpaul.winkel.util;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class MathUtil {

    public static int conjugate(int value) {
        if (value == Integer.MIN_VALUE)
            return Integer.MAX_VALUE;
        if (value == Integer.MAX_VALUE)
            return Integer.MIN_VALUE;
        return -value;
    }

    public static Vec3d clampAABB(Box aabb, Vec3d origin) {
        return new Vec3d(
                MathHelper.clamp(origin.getX(), aabb.minX, aabb.maxX),
                MathHelper.clamp(origin.getY(), aabb.minY, aabb.maxY),
                MathHelper.clamp(origin.getZ(), aabb.minZ, aabb.maxZ)
        );
    }

    public static void getRotations(float[] rotations, Vec3d from, Vec3d to) {
        Vec3d delta = to.subtract(from);

        rotations[0] = MathHelper.wrapDegrees((float) Math.toDegrees(MathHelper.atan2(delta.getZ(), delta.getX())) - 90F);
        rotations[1] = MathHelper.wrapDegrees((float) -Math.toDegrees(MathHelper.atan2(delta.getY(), delta.horizontalLength())));
    }

    public static void fixSensitivity(int fps, double sensitivity, float[] targetRotations, float[] serverRotations) {
        double sensitivityModifier = sensitivity * 0.6F + 0.2F;
        double gcd = sensitivityModifier * sensitivityModifier * sensitivityModifier;
        gcd *= 8.0;

        float deltaYaw = MathHelper.subtractAngles(serverRotations[0], targetRotations[0]);
        float deltaPitch = MathHelper.subtractAngles(serverRotations[1], targetRotations[1]);

        int iterations = (int) Math.max(1, Math.round(fps / 20D));


        while (iterations > 1) {
            double cursorDeltaX = (deltaYaw / gcd) / 0.15D;
            cursorDeltaX /= iterations;
            cursorDeltaX = Math.round(cursorDeltaX);
            cursorDeltaX *= gcd;

            double cursorDeltaY = (deltaPitch / gcd) / 0.15D;
            cursorDeltaY /= iterations;
            cursorDeltaY = Math.round(cursorDeltaY);
            cursorDeltaY *= gcd;

            serverRotations[0] += (float) cursorDeltaX * 0.15F;
            serverRotations[1] += (float) cursorDeltaY * 0.15F;
            serverRotations[1] = MathHelper.clamp(serverRotations[1], -90, 90);

            iterations--;
        }

        float smallestBalance = Float.MAX_VALUE;
        int bestBalance = 0;

        for (int balanceX = -1; balanceX <= 1; balanceX++) {
            deltaYaw = MathHelper.subtractAngles(serverRotations[0], targetRotations[0]);
            double cursorDeltaX = (deltaYaw / gcd) / 0.15D;
            cursorDeltaX = Math.round(cursorDeltaX) + balanceX;
            cursorDeltaX *= gcd;
            float yaw = serverRotations[0] + (float) cursorDeltaX * 0.15F;
            float diff = Math.abs(MathHelper.subtractAngles(yaw, targetRotations[0]));

            if (diff >= smallestBalance)
                continue;

            smallestBalance = diff;
            bestBalance = balanceX;
        }

        double cursorDeltaX = (deltaYaw / gcd) / 0.15D;
        cursorDeltaX = Math.round(cursorDeltaX) + bestBalance;
        cursorDeltaX *= gcd;

        smallestBalance = Float.MAX_VALUE;
        bestBalance = 0;

        for (int balanceY = -1; balanceY <= 1; balanceY++) {
            deltaPitch = MathHelper.subtractAngles(serverRotations[1], targetRotations[1]);
            double cursorDeltaY = (deltaPitch / gcd) / 0.15D;
            cursorDeltaY = Math.round(cursorDeltaY) + balanceY;
            cursorDeltaY *= gcd;
            float pitch = serverRotations[1] + (float) cursorDeltaY * 0.15F;
            float diff = Math.abs(MathHelper.subtractAngles(pitch, targetRotations[1]));

            if (diff >= smallestBalance)
                continue;

            smallestBalance = diff;
            bestBalance = balanceY;
        }

        double cursorDeltaY = (deltaPitch / gcd) / 0.15D;
        cursorDeltaY = Math.round(cursorDeltaY) + bestBalance;
        cursorDeltaY *= gcd;

        serverRotations[0] += (float) cursorDeltaX * 0.15F;
        serverRotations[1] += (float) cursorDeltaY * 0.15F;
        serverRotations[1] = MathHelper.clamp(serverRotations[1], -90, 90);
    }

    public static float setPrecision(double x, int precision) {
        float scalar = (int) Math.pow(10, precision);
        return ((float) (int) Math.round(x * scalar)) / scalar;
    }

    public static double limitChange(double prev, double next, double speed) {
        return prev + MathHelper.clamp(next - prev, -speed, speed);
    }

    public static float nextFloat(Random random, float min, float max) {
        return MathHelper.lerp(random.nextFloat(), min, max);
    }

    public static int nextInt(Random random, int min, int max) {
        return random.nextInt(min, max);
    }

}
