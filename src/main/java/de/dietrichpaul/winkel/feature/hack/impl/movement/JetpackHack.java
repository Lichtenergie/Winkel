package de.dietrichpaul.winkel.feature.hack.impl.movement;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.property.list.BooleanProperty;
import de.dietrichpaul.winkel.util.Timer;

public final class JetpackHack extends Hack implements UpdateListener {
    private final Timer timer = new Timer();
    private BooleanProperty noVanillaKick = new BooleanProperty("No Vanilla Kick", "noVanillaKick", "", false);

    public JetpackHack() {
        super("Jetpack", "yay, a jetpack. :3", HackCategory.MOVEMENT);
        winkel.getPropertyMap().register(this, this.noVanillaKick);
    }

    @Override
    protected void onEnable() {
        WinkelClient.INSTANCE.getEventDispatcher().subscribe(UpdateListener.class, this);
    }

    @Override
    protected void onDisable() {
        WinkelClient.INSTANCE.getEventDispatcher().unsubscribe(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        if (this.client.player != null) {
            if (this.client.options.jumpKey.isPressed()) {
                if (noVanillaKick.getValue()) {
                    if (timer.hasPassed(1500)) timer.reset();
                    if (!timer.hasPassed(1000)) this.client.player.jump();
                } else this.client.player.jump();
            } else timer.reset();
        }
    }
}
