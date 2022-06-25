package de.dietrichpaul.winkel.injection.mixin.client.gui.hud;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.render.HealthListener;
import de.dietrichpaul.winkel.event.list.render.MaxHealthListener;
import de.dietrichpaul.winkel.event.list.render.RenderOverlayListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void onPostRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        WinkelClient.INSTANCE.getEventDispatcher().post(new RenderOverlayListener.RenderOverlayEvent(tickDelta, matrices));
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0))
    public double getMaxHealth(PlayerEntity instance, EntityAttribute entityAttribute) {
        float maxHealth = instance.getMaxHealth();
        if (instance instanceof ClientPlayerEntity) {
            MaxHealthListener.MaxHealthEvent event = new MaxHealthListener.MaxHealthEvent(maxHealth);
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getMaxHealth();
        } else {
            return maxHealth;
        }
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F"))
    public float getHealth(PlayerEntity instance) {
        float health = instance.getHealth();
        if (instance instanceof ClientPlayerEntity) {
            HealthListener.HealthEvent event = new HealthListener.HealthEvent(health);
            WinkelClient.INSTANCE.getEventDispatcher().post(event);
            return event.getHealth();
        } else {
            return health;
        }
    }

}
