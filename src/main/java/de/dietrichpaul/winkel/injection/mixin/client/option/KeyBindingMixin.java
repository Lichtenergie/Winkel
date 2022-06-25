package de.dietrichpaul.winkel.injection.mixin.client.option;

import de.dietrichpaul.winkel.injection.accessor.client.option.IKeyBindingMixin;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements IKeyBindingMixin {

    @Shadow private int timesPressed;

    @Override
    public void press(int times) {
        this.timesPressed += times;
    }

}
