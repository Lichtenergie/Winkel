package de.dietrichpaul.winkel.feature.hack.impl.block;

import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.injection.accessor.client.IMinecraftClientMixin;

public class FastPlaceHack extends Hack implements UpdateListener {

    public FastPlaceHack() {
        super("FastPlace", "", HackCategory.BLOCK);
    }

    @Override
    protected void onEnable() {
        winkel.getEventDispatcher().subscribe(UpdateListener.class, this);
    }

    @Override
    protected void onDisable() {
        winkel.getEventDispatcher().unsubscribe(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        ((IMinecraftClientMixin) client).setItemUseCooldown(0);
    }

}
