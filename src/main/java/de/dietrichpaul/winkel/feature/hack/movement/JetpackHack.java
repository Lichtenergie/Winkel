package de.dietrichpaul.winkel.feature.hack.movement;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.util.Timer;

public final class JetpackHack extends Hack implements UpdateListener {
    private final Timer timer = new Timer();
    private boolean noVanillaKick = true;

    public JetpackHack() {
        super("Jetpack", "yay, a jetpack. :3", HackCategory.MOVEMENT);
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
                if (noVanillaKick) {
                    if (timer.hasPassed(1500)) timer.reset();
                    if (!timer.hasPassed(1000)) this.client.player.jump();
                } else this.client.player.jump();
            } else timer.reset();
        }
    }
}
