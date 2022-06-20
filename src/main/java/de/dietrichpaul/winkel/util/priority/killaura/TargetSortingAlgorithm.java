package de.dietrichpaul.winkel.util.priority.killaura;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public interface TargetSortingAlgorithm {

    double getWeight(MinecraftClient client, Entity target);

}
