package de.dietrichpaul.winkel.feature.hack.engine.click;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.InputHandleListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.injection.accessor.client.option.IKeyBindingMixin;
import net.minecraft.client.MinecraftClient;

public class ClickEngine implements InputHandleListener, ClickCallback {

    public ClickEngine() {
        WinkelClient.INSTANCE.getEventDispatcher().subscribe(InputHandleListener.class, this);
    }

    private int attacks;
    private int interactions;

    @Override
    public void pressAttack(int times) {
        this.attacks += times;
    }

    @Override
    public void pressUse(int times) {
        this.interactions += times;
    }

    @Override
    public void onHandleInput(InputHandleEvent event) {
        InputHandler bestHandler = null;
        int bestHandlerPriority = Integer.MIN_VALUE;
        for (Hack hack : WinkelClient.INSTANCE.getHackList().getHacks()) {
            if (hack instanceof InputHandler handler && hack.isEnabled() && handler.canClick()) {
                if (bestHandler == null) {
                    bestHandler = handler;
                    continue;
                }

                int priority = handler.getClickingPriority();
                if (priority > bestHandlerPriority) {
                    bestHandlerPriority = priority;
                    bestHandler = handler;
                }
            }
        }

        if (bestHandler != null) {
            bestHandler.click(this);
        }

        ((IKeyBindingMixin) MinecraftClient.getInstance().options.attackKey).press(this.attacks);
        ((IKeyBindingMixin) MinecraftClient.getInstance().options.useKey).press(this.interactions);

        this.attacks = 0;
        this.interactions = 0;
    }

}
