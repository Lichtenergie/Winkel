package de.dietrichpaul.winkel.feature.hack.movement;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;

public final class JetpackHack extends Hack implements UpdateListener {
    private boolean spoofOnGround = true;

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
                this.client.player.jump();
            }
        }
    }
}
