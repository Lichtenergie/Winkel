package de.dietrichpaul.winkel.injection.mixin.client.renderer.entity;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedPitchListener;
import de.dietrichpaul.winkel.event.list.rotationrequest.RequestSpoofedPrevPitchListener;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
    public float getRenderPitch(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity) {
            RequestSpoofedPitchListener.RequestSpoofedPitchEvent event = new RequestSpoofedPitchListener.RequestSpoofedPitchEvent(instance.getPitch());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getPitch();
        }
        return instance.getPitch();
    }

    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;prevPitch:F"))
    public float getRenderPrevPitch(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity) {
            RequestSpoofedPrevPitchListener.RequestSpoofedPrevPitchEvent event = new RequestSpoofedPrevPitchListener.RequestSpoofedPrevPitchEvent(instance.prevPitch);
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getPrevPitch();
        }
        return instance.prevPitch;
    }

    @Redirect(method = "setupTransforms", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
    public float getRiptidePitch(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity) {
            RequestSpoofedPitchListener.RequestSpoofedPitchEvent event = new RequestSpoofedPitchListener.RequestSpoofedPitchEvent(instance.getPitch());
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getPitch();
        }
        return instance.getPitch();
    }

}
