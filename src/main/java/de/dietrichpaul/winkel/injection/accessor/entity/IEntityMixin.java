package de.dietrichpaul.winkel.injection.accessor.entity;

import net.minecraft.util.math.Vec3d;

public interface IEntityMixin {

    Vec3d accessMovementInputToVelocity(Vec3d movementInput, float speed, float yaw);

}
