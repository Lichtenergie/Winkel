package de.dietrichpaul.winkel.feature.pattern.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;

public interface RotationPattern extends StringIdentifiable {

    void generateRotations(Vec3d camera, Entity target, float[] previous, float[] decelerate, float[] rotations);

}
