package de.dietrichpaul.winkel.injection.mixin.entity;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedPitchListener;
import de.dietrichpaul.winkel.injection.accessor.client.network.IClientPlayerEntityMixin;
import de.dietrichpaul.winkel.injection.accessor.entity.IEntityMixin;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityMixin {

    @Shadow
    private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        return null;
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getPitch()F"))
    public float requestPitch(Entity instance) {
        if (instance instanceof ClientPlayerEntity clientPlayer) {
            RequestSpoofedPitchListener.RequestSpoofedPitchEvent event = new RequestSpoofedPitchListener.RequestSpoofedPitchEvent(instance.getPitch());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getPitch();
        }
        return instance.getPitch();
    }

    @Override
    public Vec3d accessMovementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        return movementInputToVelocity(movementInput, speed, yaw);
    }

}
