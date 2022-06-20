package de.dietrichpaul.winkel.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathFixer {

    public static void moveFlying(MinecraftClient mc, float strafe, float forward, float yaw, double[] motions) {
        float f4 = 0.91F;
        if (mc.player.isOnGround()) {
            f4 = mc.world.getBlockState(new BlockPos(MathHelper.floor(mc.player.getX()), MathHelper.floor(mc.player.getY()) - 1, MathHelper.floor(mc.player.getZ()))).getBlock().getSlipperiness() * 0.91F;
        }
        float inputLength = MathHelper.sqrt(strafe * strafe + forward * forward);
        if (inputLength < 1.0F)
            inputLength = 1.0F;
        inputLength = f4 / inputLength;
        strafe *= inputLength;
        forward *= inputLength;
        float f1 = MathHelper.sin(yaw * 3.141592F / 180F);
        float f2 = MathHelper.cos(yaw * 3.141592F / 180F);
        motions[0] += strafe * f2 - forward * f1;
        motions[2] += forward * f2 + strafe * f1;
    }

}
