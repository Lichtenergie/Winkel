package de.dietrichpaul.winkel.feature.hack.impl.movement;

import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.property.list.FloatProperty;
import net.minecraft.util.math.Vec3d;

public class SpiderHack extends Hack implements UpdateListener {

    private FloatProperty boost = new FloatProperty("Boost", "boost", "", 0.3F, 0F, 10F, 0.05F);

    public SpiderHack() {
        super("Spider", "", HackCategory.MOVEMENT);
        winkel.getPropertyMap().register(this, this.boost);
    }

    @Override
    public void onUpdate() {
        if (!client.player.horizontalCollision)
            return;
        Vec3d velocity = client.player.getVelocity();
        client.player.setVelocity(velocity.x, boost.getValue(), velocity.z);
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
