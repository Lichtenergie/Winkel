package de.dietrichpaul.winkel.feature.pattern.rotation.impl;

import de.dietrichpaul.winkel.feature.pattern.rotation.AbstractRotationPattern;
import de.dietrichpaul.winkel.util.function.FloatSupplier;
import de.dietrichpaul.winkel.util.math.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ClosestPattern extends AbstractRotationPattern {

    private FloatSupplier marginSupplier;

    public ClosestPattern(FloatSupplier marginSupplier) {
        super("Closest");
        this.marginSupplier = marginSupplier;
    }

    @Override
    public void generateRotations(Vec3d camera, Entity target, float[] previous, float[] decelerate, float[] rotations) {
        Box aabb = target.getBoundingBox().expand(target.getTargetingMargin() * this.marginSupplier.getAsFloat());
        Vec3d closest = MathUtil.clampAABB(aabb, camera);
        MathUtil.getRotations(rotations, camera, closest);
    }

}
