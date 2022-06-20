package de.dietrichpaul.winkel.injection.mixin.client.network;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.rotationrequest.ForceSpoofRotationListener;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setYaw(F)V", ordinal = 0))
    public void setYaw(ClientPlayerEntity instance, float v) {
        WinkelClient.INSTANCE.getEventDispatcher()
                .post(new ForceSpoofRotationListener.ForceSpoofRotationEvent(v, 0, true, false));
        instance.setYaw(v);
    }

    @Inject(method = "onPlayerRespawn", at = @At("RETURN"))
    public void onRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        WinkelClient.INSTANCE.getEventDispatcher()
                .post(new ForceSpoofRotationListener.ForceSpoofRotationEvent(-180.0f, 0, true, false));
    }

}
