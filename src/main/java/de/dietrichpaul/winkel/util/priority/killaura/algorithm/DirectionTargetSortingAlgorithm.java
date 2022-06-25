package de.dietrichpaul.winkel.util.priority.killaura.algorithm;

import de.dietrichpaul.winkel.util.math.MathUtil;
import de.dietrichpaul.winkel.util.priority.killaura.TargetSortingAlgorithm;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DirectionTargetSortingAlgorithm implements TargetSortingAlgorithm {

    @Override
    public double getWeight(MinecraftClient client, Entity target) {
        if (client.crosshairTarget instanceof EntityHitResult ehr
                && target.equals(ehr.getEntity())) {
            return 0;
        }

        Vec3d camera = client.player.getCameraPosVec(1.0F);
        Vec3d hitVec = target.getBoundingBox().getCenter();

        float[] rotations = new float[2];
        MathUtil.getRotations(rotations, camera, hitVec);

        float deltaYaw = MathHelper.angleBetween(client.player.getYaw(), rotations[0]);
        float deltaPitch = MathHelper.angleBetween(client.player.getPitch(), rotations[1]);

        return Math.hypot(deltaPitch, deltaYaw);
    }

}
