package de.dietrichpaul.winkel.injection.mixin.client.network;

import com.mojang.authlib.GameProfile;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.movement.SprintHack;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.BEFORE))
    public void onTick(CallbackInfo ci) {
        WinkelClient.INSTANCE.getEventDispatcher().post(UpdateListener.UpdateEvent.INSTANCE);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    public boolean onValidateSprintForward(Input instance) {
        SprintHack sprint = WinkelClient.INSTANCE.getHackList().sprint;
        if (sprint.isEnabled() && sprint.isInAllDirection()) {
            return instance.movementForward != 0 || instance.movementSideways != 0;
        }
        return instance.hasForwardMovement();
    }

    @Override
    public boolean hasStatusEffect(StatusEffect effect) {
        if (effect == StatusEffects.NIGHT_VISION && WinkelClient.INSTANCE.getHackList().fullBright.isEnabled())
            return true;
        return super.hasStatusEffect(effect);
    }

}
