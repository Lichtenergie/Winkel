package de.dietrichpaul.winkel.feature.pattern.rotation.impl;

import de.dietrichpaul.winkel.feature.pattern.rotation.AbstractRotationPattern;
import de.dietrichpaul.winkel.util.function.FloatSupplier;
import de.dietrichpaul.winkel.util.math.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class NearestPattern extends AbstractRotationPattern {

    private FloatSupplier marginSupplier;

    public NearestPattern(FloatSupplier marginSupplier) {
        super("Nearest");
        this.marginSupplier = marginSupplier;
    }

    @Override
    public void generateRotations(Vec3d camera, Entity target, float[] previous, float[] decelerate, float[] rotations) {
        Box aabb = target.getBoundingBox().expand(target.getTargetingMargin() * this.marginSupplier.getAsFloat());
        double scalar = camera.distanceTo(aabb.getCenter());
        Vec3d polarRotations = Vec3d.fromPolar(previous[1], previous[0]);
        Vec3d prevHitVec = camera.add(polarRotations.multiply(scalar));
        Vec3d nearest = MathUtil.clampAABB(aabb, prevHitVec);

        MathUtil.getRotations(rotations, camera, nearest);
    }

}
