package de.dietrichpaul.winkel.util.raytrace;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;

public class RayTraceUtil {

    public static RayTraceResult rayTraceEntities(MinecraftClient client, float[] rotation, float[] prevRotation, float blockReach, float entityReach, float tickDelta) {
        Entity rayTracer = client.getCameraEntity();
        if (rayTracer == null || client.player == null)
            return null;

        Entity targetedEntity = null;
        double distance = blockReach;
        Vec3d start = rayTracer.getCameraPosVec(tickDelta);
        HitResult collision = null;
        boolean hasExtendedReach = false;
        double combatReachExtension = distance;
        if (client.interactionManager.hasExtendedReach()) {
            combatReachExtension = distance = 6.0D;
        } else {
            if (distance > 3.0D)
                hasExtendedReach = true;
        }

        combatReachExtension *= combatReachExtension;

        Vec3d polar = Vec3d.fromPolar(rotation[1], rotation[0]);
        Vec3d polarEnd = polar.multiply(distance);
        Vec3d end = start.add(polarEnd);
        Box boxWithPossibleCollisions = rayTracer.getBoundingBox().expand(50);
        EntityHitResult entityCollision = ProjectileUtil.raycast(rayTracer, start, end, boxWithPossibleCollisions,
                entity -> !entity.isSpectator() && entity.collides(), combatReachExtension);
        if (entityCollision == null)
            return new RayTraceResult(collision, null);

        Entity collidedEntity = entityCollision.getEntity();
        Vec3d hitVec = entityCollision.getPos();
        double rayLength = start.squaredDistanceTo(hitVec);
        if (hasExtendedReach && rayLength > (entityReach * entityReach)) {
            return new RayTraceResult(BlockHitResult.createMissed(hitVec, Direction.getFacing(polar.x, polar.y, polar.z), new BlockPos(hitVec)), null);
        } else if (rayLength < combatReachExtension || collision == null) {
            collision = entityCollision;
            if (collidedEntity instanceof LivingEntity || collidedEntity instanceof ItemFrameEntity) {
                targetedEntity = collidedEntity;
            }
        }

        return new RayTraceResult(collision, targetedEntity);
    }

    public static RayTraceResult rayTrace(MinecraftClient client, float[] rotation, float[] prevRotation, float blockReach, float entityReach, float tickDelta) {
        Entity rayTracer = client.getCameraEntity();
        if (rayTracer == null || client.player == null)
            return null;

        Entity targetedEntity = null;
        double distance = blockReach;
        Vec3d start = rayTracer.getCameraPosVec(tickDelta);
        HitResult collision = rayTraceBlocks(rayTracer, start, rotation, prevRotation, distance, tickDelta, false);
        boolean hasExtendedReach = false;
        double combatReachExtension = distance;
        if (client.interactionManager.hasExtendedReach()) {
            combatReachExtension = distance = 6.0D;
        } else {
            if (distance > 3.0D)
                hasExtendedReach = true;
        }

        combatReachExtension *= combatReachExtension;
        if (collision != null) {
            combatReachExtension = collision.getPos().squaredDistanceTo(start);
        }

        Vec3d polar = Vec3d.fromPolar(rotation[1], rotation[0]);
        Vec3d polarEnd = polar.multiply(distance);
        Vec3d end = start.add(polarEnd);
        Box boxWithPossibleCollisions = rayTracer.getBoundingBox().expand(50);
        EntityHitResult entityCollision = ProjectileUtil.raycast(rayTracer, start, end, boxWithPossibleCollisions,
                entity -> !entity.isSpectator() && entity.collides(), combatReachExtension);
        if (entityCollision == null)
            return new RayTraceResult(collision, null);

        Entity collidedEntity = entityCollision.getEntity();
        Vec3d hitVec = entityCollision.getPos();
        double rayLength = start.squaredDistanceTo(hitVec);
        if (hasExtendedReach && rayLength > (entityReach * entityReach)) {
            return new RayTraceResult(BlockHitResult.createMissed(hitVec, Direction.getFacing(polar.x, polar.y, polar.z), new BlockPos(hitVec)), null);
        } else if (rayLength < combatReachExtension || collision == null) {
            collision = entityCollision;
            if (collidedEntity instanceof LivingEntity || collidedEntity instanceof ItemFrameEntity) {
                targetedEntity = collidedEntity;
            }
        }

        return new RayTraceResult(collision, targetedEntity);
    }

    public static BlockHitResult rayTraceBlocks(Entity rayTracer, Vec3d start, float[] rotations, float[] prevRotations, double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d polar = Vec3d.fromPolar(
                MathHelper.lerp(tickDelta, prevRotations[1], rotations[1]),
                MathHelper.lerp(tickDelta, prevRotations[0], rotations[0])
        );
        Vec3d end = start.add(polar.x * maxDistance, polar.y * maxDistance, polar.z * maxDistance);
        return rayTracer.world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, rayTracer));
    }

}
