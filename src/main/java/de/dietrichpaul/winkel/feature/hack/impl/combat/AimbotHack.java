package de.dietrichpaul.winkel.feature.hack.impl.combat;

import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.feature.hack.engine.rotation.EntityAimbot;
import de.dietrichpaul.winkel.property.list.FloatProperty;
import de.dietrichpaul.winkel.util.MathUtil;
import de.dietrichpaul.winkel.util.raytrace.RayTraceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AimbotHack extends EntityAimbot {

    public AimbotHack() {
        super("Aimbot", "", HackCategory.COMBAT);
    }

    private FloatProperty rangeProperty = new FloatProperty("Range", "range", "", 3, 1, 12, 0.05F);
    private FloatProperty aimRangeProperty = new FloatProperty("Aim range", "aimRange", "", 1.5F, 1, 6, 0.25F);
    private FloatProperty throughWallRangeProperty = new FloatProperty("Through wall range", "throughWallRange",
            "Determines how far Killaura will reach", 0, 0, 12, 0.05F);

    @Override
    protected float getCombatRange() {
        return this.rangeProperty.getValue();
    }

    @Override
    public HitResult getTarget() {
        if (this.target == null) {
            return RayTraceUtil.rayTrace(client, this.rotations, this.rotations, client.interactionManager.getReachDistance(), this.rangeProperty.getValue(), 1).collision();
        }
        Vec3d closest = MathUtil.clampAABB(this.target.getBoundingBox().expand(this.target.getTargetingMargin()), client.player.getCameraPosVec(1.0F));
        boolean canHit = client.player.getCameraPosVec(1.0F).distanceTo(closest) < rangeProperty.getValue();
        if (!client.player.canSee(this.target)) {
            canHit = client.player.getCameraPosVec(1.0F).distanceTo(closest) < throughWallRangeProperty.getValue();
        }
        if (canHit) {
            return new EntityHitResult(this.target, this.target.getBoundingBox().getCenter());
        } else {
            return RayTraceUtil.rayTrace(client, this.rotations, this.rotations, client.interactionManager.getReachDistance(), this.rangeProperty.getValue(), 1).collision();
        }
    }

    @Override
    protected boolean filter(Entity entity) {
        Vec3d camera = client.player.getCameraPosVec(1.0F);
        float aimRange2 = MathHelper.square(this.rangeProperty.getValue() + this.aimRangeProperty.getValue());

        if (MathUtil.clampAABB(entity.getBoundingBox().expand(entity.getTargetingMargin()), camera).squaredDistanceTo(camera) > aimRange2)
            return false;

        boolean found = throughWallRangeProperty.getValue() > 0;

        double clip = 1 / 6D;
        Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());

        bruteForce:
        if (!found) {
            for (double x = 0; x <= 1; x += clip) {
                for (double y = 0; y <= 1; y += clip) {
                    for (double z = 0; z <= 1; z += clip) {
                        Vec3d lerpVec = new Vec3d(
                                MathHelper.lerp(x, box.minX, box.maxX),
                                MathHelper.lerp(y, box.minY, box.maxY),
                                MathHelper.lerp(z, box.minZ, box.maxZ)
                        );
                        float[] tempRotations = new float[2];
                        MathUtil.getRotations(tempRotations, camera, lerpVec);
                        if (RayTraceUtil.rayTrace(client, tempRotations, tempRotations, client.interactionManager.getReachDistance(), rangeProperty.getValue() + aimRangeProperty.getValue(), 1F).collision().getType() != HitResult.Type.ENTITY)
                            continue;
                        found = true;
                        break bruteForce;
                    }
                }
            }
        }

        return found;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void tickEngine(float[] previous, float[] decelerate, float[] rotations) {
        Vec3d camera = client.player.getCameraPosVec(1.0F);
        MathUtil.getRotations(rotations, camera, this.target.getCameraPosVec(1.0F));
    }

}