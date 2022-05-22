package de.dietrichpaul.winkel.feature.hack.movement;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public final class NoFallHack extends Hack implements UpdateListener {
    private final String[] modes = {
            "Spoof",
            "AAC" // todo
    };
    private String currentMode = "Spoof";

    public NoFallHack() {
        super("NoFall", "Nu fall-damage for u anymore. x)", HackCategory.MOVEMENT);
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
            switch (this.currentMode) {
                case "Spoof" -> {
                    if (this.client.player.fallDistance > .4F) {
                        this.client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
                        this.client.player.fallDistance = .0F;
                    }
                }
                case "AAC" -> {
                    // todo
                }
            }
        }
    }
}
