package de.dietrichpaul.winkel.feature.pattern.click.impl;

import de.dietrichpaul.winkel.feature.pattern.click.ClickPattern;
import de.dietrichpaul.winkel.util.math.MathUtil;
import net.minecraft.util.math.MathHelper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;

public class CustomClickPattern implements ClickPattern {

    private String name;

    private IntSupplier maxCPSSupplier;
    private IntSupplier minCPSSupplier;

    public CustomClickPattern(String name, IntSupplier maxCPSSupplier, IntSupplier minCPSSupplier) {
        this.name = name;
        this.maxCPSSupplier = maxCPSSupplier;
        this.minCPSSupplier = minCPSSupplier;
    }

    @Override
    public int[][] generateMatrix(Random random) {
        int[] cps = new int[40];
        for (int i = 0; i < cps.length; i++) {
            cps[i] = MathUtil.nextInt(random, minCPSSupplier.getAsInt(), maxCPSSupplier.getAsInt());
        }

        int[][] matrix = new int[cps.length][20];

        for (int zeile = 0; zeile < cps.length; zeile++) {
            double index = 0.0;
            double abstand = 20.0 / cps[zeile];
            while (cps[zeile] > 0) {
                matrix[zeile][MathHelper.floor(index)]++;
                cps[zeile]--;
                index += abstand;
                if (index >= 20) {
                    index = 0;
                }
            }
            if (ThreadLocalRandom.current().nextBoolean())
                MathUtil.shuffle(ThreadLocalRandom.current(), x -> x * x * x * x * x, matrix[zeile]);
            if (ThreadLocalRandom.current().nextBoolean())
                MathUtil.offset(matrix[zeile], MathUtil.nextInt(ThreadLocalRandom.current(), -2, 3));

        }
        return matrix;
    }

    @Override
    public String asString() {
        return this.name;
    }

}
