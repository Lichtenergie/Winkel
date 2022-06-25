package de.dietrichpaul.winkel.injection.mixin.client.input;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.tick.move.StrafeInputEmulationListener;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Shadow
    @Final
    private GameOptions settings;

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(boolean slowDown, float slowness, CallbackInfo ci) {
        StrafeInputEmulationListener.StrafeInputEmulationEvent event
                = new StrafeInputEmulationListener.StrafeInputEmulationEvent(Math.signum(this.movementSideways), Math.signum(this.movementForward), slowDown, slowness);
        WinkelClient.INSTANCE.getEventDispatcher().post(event);

        this.movementSideways = event.getMovementSideways();
        this.movementForward = event.getMovementForward();
        if (event.hasSlowdown()) {
            this.movementForward *= event.getSlowness();
            this.movementSideways *= event.getSlowness();
        }

        this.pressingBack = this.movementForward < 0;
        this.pressingForward = this.movementForward > 0;
        this.pressingLeft = this.movementSideways > 0;
        this.pressingRight = this.movementSideways < 0;
    }

}
