package de.dietrichpaul.winkel.feature.hack.impl.movement;

import de.dietrichpaul.winkel.event.list.ExtensionPickTargetListener;
import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.BooleanProperty;
import de.dietrichpaul.winkel.util.math.MathUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;

public class WTapHack extends Hack implements UpdateListener {

    private BooleanProperty jump = new BooleanProperty("Jump", "jump", "", false);
    private BooleanProperty debug = new BooleanProperty("Debug", "debug", "", false);
    private boolean effecting;

    public WTapHack() {
        super("WTap", "", HackCategory.COMBAT);
    }

    @Override
    protected void makeProperties(PropertyMap map) {
        map.register(this, this.jump);
        map.register(this, this.debug);
        super.makeProperties(map);
    }

    @Override
    protected void onEnable() {
        events.subscribe(UpdateListener.class, this);
    }

    @Override
    protected void onDisable() {
        events.unsubscribe(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        LivingEntity target = null;
        if (client.crosshairTarget instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity living)
            target = living;
        ExtensionPickTargetListener.ExtensionPickTargetEvent event = new ExtensionPickTargetListener.ExtensionPickTargetEvent(target);
        events.post(event);
        target = event.getTarget();

        if (this.effecting && target == null) {
            client.options.backKey.setPressed(false);
            this.effecting = false;
        }

        if (target == null)
            return;

        int offset = MathUtil.getLatencyTicks(client);

        if (target.hurtTime == 2 + offset) {
            if (Math.signum(client.player.input.movementForward) == 1) {
                this.effecting = true;
                client.options.backKey.setPressed(true);
                if (debug.getValue())
                    chat.print("hack.wtap.stop");
            }
        } else if (target.hurtTime == 1 + offset) {
            if (this.effecting) {
                client.options.backKey.setPressed(false);
                this.effecting = false;
                if (debug.getValue())
                    chat.print("hack.wtap.sprinting");
            }
        }
    }

}
