package de.dietrichpaul.winkel.util.priority.killaura.algorithm;

import de.dietrichpaul.winkel.util.priority.killaura.TargetSortingAlgorithm;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class ClosestTargetSortingAlgorithm implements TargetSortingAlgorithm {

    @Override
    public double getWeight(MinecraftClient client, Entity target) {
        return client.player.squaredDistanceTo(target);
    }

}
