package de.dietrichpaul.winkel.feature.hack.movement;

import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;

public class StepHack extends Hack implements UpdateListener {

    public StepHack() {
        super("Step", "bro", HackCategory.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        winkel.getEventDispatcher().subscribe(UpdateListener.class, this);
    }

    @Override
    protected void onDisable() {
        winkel.getEventDispatcher().unsubscribe(UpdateListener.class, this);
        client.player.stepHeight = 0.6F;
    }

    @Override
    public void onUpdate() {
        client.player.stepHeight = 1F;
    }

}
