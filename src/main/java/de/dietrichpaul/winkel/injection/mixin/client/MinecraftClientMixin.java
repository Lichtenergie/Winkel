package de.dietrichpaul.winkel.injection.mixin.client;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.injection.accessor.client.IMinecraftClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class, priority = Integer.MIN_VALUE)
public class MinecraftClientMixin implements IMinecraftClientMixin {

    @Shadow private int itemUseCooldown;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setErrorCallback(Lorg/lwjgl/glfw/GLFWErrorCallbackI;)V", shift = At.Shift.AFTER))
    public void onInit(RunArgs args, CallbackInfo ci) {
        WinkelClient.INSTANCE.start();
    }

    @Override
    public int getItemUseCooldown() {
        return this.itemUseCooldown;
    }

    @Override
    public void setItemUseCooldown(int ticks) {
        this.itemUseCooldown = ticks;
    }

}
