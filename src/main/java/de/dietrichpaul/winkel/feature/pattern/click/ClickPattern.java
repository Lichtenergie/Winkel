package de.dietrichpaul.winkel.feature.pattern.click;

import net.minecraft.util.StringIdentifiable;

import java.util.Random;

public interface ClickPattern extends StringIdentifiable {

    int[][] generateMatrix(Random random);

}
