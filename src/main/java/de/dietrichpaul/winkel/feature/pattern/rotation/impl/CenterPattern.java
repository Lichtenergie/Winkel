package de.dietrichpaul.winkel.feature.pattern.rotation.impl;

import de.dietrichpaul.winkel.feature.pattern.rotation.AbstractRotationPattern;
import de.dietrichpaul.winkel.util.math.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class CenterPattern extends AbstractRotationPattern {

    public CenterPattern() {
        super("Center");
    }

    @Override
    public void generateRotations(Vec3d camera, Entity target, float[] previous, float[] decelerate, float[] rotations) {
        MathUtil.getRotations(rotations, camera, target.getBoundingBox().getCenter());
    }

}
