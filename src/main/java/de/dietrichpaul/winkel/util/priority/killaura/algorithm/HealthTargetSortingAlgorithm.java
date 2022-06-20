package de.dietrichpaul.winkel.util.priority.killaura.algorithm;

import de.dietrichpaul.winkel.util.priority.killaura.TargetSortingAlgorithm;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class HealthTargetSortingAlgorithm implements TargetSortingAlgorithm {

    @Override
    public double getWeight(MinecraftClient client, Entity target) {
        if (target instanceof LivingEntity living)
            return living.getHealth();
        return Double.MAX_VALUE;
    }

}
