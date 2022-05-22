package de.dietrichpaul.winkel.util;

public class MathUtil {

    public static int conjugate(int value) {
        if (value == Integer.MIN_VALUE)
            return Integer.MAX_VALUE;
        if (value == Integer.MAX_VALUE)
            return Integer.MIN_VALUE;
        return -value;
    }

}
