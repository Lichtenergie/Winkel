package de.dietrichpaul.winkel.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class BlockUtils {

    private static VoxelShape getOutlineShape(World world, BlockPos pos) {
        return getState(world, pos).getOutlineShape(world, pos);
    }

    public static BlockState getState(World world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    public static Block getBlock(World world, BlockPos pos) {
        return getState(world, pos).getBlock();
    }

    public static Box getBoundingBox(World world, BlockPos pos) {
        return getOutlineShape(world, pos).getBoundingBox().offset(pos);
    }

    public static boolean canBeClicked(World world, BlockPos pos) {
        return getOutlineShape(world, pos) != VoxelShapes.empty();
    }

    public static boolean isReplaceable(World world, BlockPos blockPos) {
        return getState(world, blockPos).getMaterial().isReplaceable();
    }

    public static boolean isConnected(World world, BlockPos blockPos) {
        for (Direction direction : Direction.values())
            if (!isReplaceable(world, blockPos.offset(direction)))
                return true;
        return false;
    }

    public static BlockHitResult getBlockThatPlacedHere(World world, Vec3d camera, BlockPos result, Direction priority) {
        double smallest = 64;
        BlockPos outAlign = null;
        Direction outDirection = null;
        if (priority != null && priority != Direction.UP) {
            BlockPos align = result.offset(priority);
            Vec3d alignCenter = Vec3d.ofCenter(align);
            double dist = camera.squaredDistanceTo(alignCenter);
            if (dist < smallest && !isReplaceable(world, align)) {
                smallest = dist;
                outDirection = priority;
                outAlign = align;
            }
        }
        for (Direction direction : Direction.values()) {
            if (direction == Direction.UP)
                continue;
            BlockPos align = result.offset(direction);
            Vec3d alignCenter = Vec3d.ofCenter(align);
            double dist = camera.squaredDistanceTo(alignCenter);
            if (dist >= smallest)
                continue; // wenn die seite vom block nicht sichtbar ist
            if (isReplaceable(world, align))
                continue; // wenn der block beim rechtsklicken verschwinden würde
            smallest = dist;
            outDirection = direction;
            outAlign = align;
        }
        if (outAlign == null || outDirection == null) return null;
        return new BlockHitResult(Vec3d.of(outAlign), outDirection.getOpposite(), outAlign, false);
    }

}
