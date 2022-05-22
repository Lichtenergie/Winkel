package de.dietrichpaul.winkel.feature.hack.movement;

import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import net.minecraft.util.math.Vec3d;

public class SpiderHack extends Hack implements UpdateListener {

    public SpiderHack() {
        super("Spider", "", HackCategory.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (!client.player.horizontalCollision)
            return;
        Vec3d velocity = client.player.getVelocity();
        client.player.setVelocity(velocity.x, 0.3, velocity.z);
    }

    @Override
    protected void onEnable() {
        winkel.getEventDispatcher().subscribe(UpdateListener.class, this);
    }

    @Override
    protected void onDisable() {
        winkel.getEventDispatcher().unsubscribe(UpdateListener.class, this);
    }

}
