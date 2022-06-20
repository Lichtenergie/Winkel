package de.dietrichpaul.winkel.injection.mixin.entity;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedYawListener;
import de.dietrichpaul.winkel.injection.accessor.client.network.IClientPlayerEntityMixin;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Redirect(method = "tickNewAi", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getYaw()F"))
    public float redirectBodyYaw(PlayerEntity instance) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            RequestSpoofedYawListener.RequestSpoofedYawEvent event = new RequestSpoofedYawListener.RequestSpoofedYawEvent(instance.getYaw());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getYaw();
        }
        return instance.getYaw();
    }

}
