package de.dietrichpaul.winkel.feature.hack.impl.movement;

import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import net.minecraft.client.option.KeyBinding;

public class SprintHack extends Hack implements UpdateListener {

    public SprintHack() {
        super("Sprint", "", HackCategory.MOVEMENT);
    }

    private boolean inAllDirection = true;

    @Override
    protected void onEnable() {
        winkel.getEventDispatcher().subscribe(UpdateListener.class, this);
    }

    @Override
    protected void onDisable() {
        winkel.getEventDispatcher().unsubscribe(UpdateListener.class, this);
        KeyBinding.updatePressedStates();
    }

    @Override
    public void onUpdate() {
        if (client.player.input.movementForward == 0 && client.player.input.movementSideways == 0)
            return;
        if (inAllDirection) {
            client.player.setSprinting(true);
        } else {
            client.options.sprintKey.setPressed(true);
        }
    }

    public boolean isInAllDirection() {
        return inAllDirection;
    }

}
