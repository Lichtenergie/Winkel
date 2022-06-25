package de.dietrichpaul.winkel.feature.pattern.click.impl;

import de.dietrichpaul.winkel.feature.pattern.click.ClickPattern;
import de.dietrichpaul.winkel.util.math.MathUtil;

import java.util.List;
import java.util.Random;

public class FileClickPattern implements ClickPattern {

    private String name;
    private List<int[]> lines;

    public FileClickPattern(String name, List<int[]> lines) {
        this.name = name;
        this.lines = lines;
    }

    @Override
    public int[][] generateMatrix(Random random) {
        int[][] matrix = new int[Math.min(this.lines.size(), 40)][20];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = this.lines.get(MathUtil.nextInt(random, 0, this.lines.size()));
        }
        return matrix;
    }

    @Override
    public String asString() {
        return this.name;
    }

}
