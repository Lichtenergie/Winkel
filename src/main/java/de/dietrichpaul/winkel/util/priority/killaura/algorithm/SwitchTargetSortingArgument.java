package de.dietrichpaul.winkel.util.priority.killaura.algorithm;

import de.dietrichpaul.winkel.util.MathUtil;
import de.dietrichpaul.winkel.util.priority.killaura.TargetSortingAlgorithm;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class SwitchTargetSortingArgument implements TargetSortingAlgorithm {

    @Override
    public double getWeight(MinecraftClient client, Entity target) {
        Vec3d camera = client.player.getCameraPosVec(1.0F);
        Box box = target.getBoundingBox().expand(target.getTargetingMargin());
        if (target instanceof LivingEntity living)
            return (living.hurtTime + 1) * camera.squaredDistanceTo(MathUtil.clampAABB(box, camera));
        return 0;
    }

}
