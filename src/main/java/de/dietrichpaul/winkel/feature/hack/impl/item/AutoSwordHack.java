package de.dietrichpaul.winkel.feature.hack.impl.item;

import de.dietrichpaul.winkel.event.list.ExtensionPickTargetListener;
import de.dietrichpaul.winkel.event.list.InputHandleListener;
import de.dietrichpaul.winkel.event.list.raytrace.PostRayTraceListener;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.BooleanProperty;
import de.dietrichpaul.winkel.property.list.IntegerProperty;
import de.dietrichpaul.winkel.util.ItemUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.hit.EntityHitResult;

public class AutoSwordHack extends Hack implements InputHandleListener, ExtensionPickTargetListener {

    public AutoSwordHack() {
        super("AutoSword", "", HackCategory.ITEM);
    }

    private final BooleanProperty legacy = new BooleanProperty("Legacy", "legacy", "", false);
    private final IntegerProperty holdTicks = new IntegerProperty("HoldTicks", "holdTicks", "", 20, 0, 40);

    private int prev;
    private int ticks;
    private boolean swap;

    @Override
    protected void onEnable() {
        events.subscribe(InputHandleListener.class, this);
        events.subscribe(ExtensionPickTargetListener.class, this);
        this.swap = false;
        this.prev = -1;
    }

    @Override
    protected void onDisable() {
        events.unsubscribe(InputHandleListener.class, this);
        events.unsubscribe(ExtensionPickTargetListener.class, this);
        if (this.swap) {
            client.player.getInventory().selectedSlot = this.prev;
        }
    }

    @Override
    protected void makeProperties(PropertyMap map) {
        addProperty(map, this.legacy);
        addProperty(map, this.holdTicks);
        super.makeProperties(map);
    }

    @Override
    public void onHandleInput(InputHandleEvent event) {
        if (client.crosshairTarget instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity) {
            this.select();
        } else {
            this.swap = false;
        }
        if (!this.swap) {
            if (this.ticks > 0) {
                this.ticks--;
                return;
            }
            if (this.ticks == 0) {
                client.player.getInventory().selectedSlot = this.prev;
                this.prev = -1;
                this.ticks--;
            }
        }
    }

    public void select() {
        if (!isEnabled())
            return;

        int weaponSlot = -1;
        float weaponValue = Float.MIN_VALUE;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof MiningToolItem || stack.getItem() instanceof SwordItem))
                continue;
            float value = ItemUtils.getWeaponValue(this.legacy.getValue(), client.player, stack);
            if (value <= weaponValue)
                continue;

            weaponValue = value;
            weaponSlot = i;
        }

        if (weaponSlot == -1) {
            this.swap = false;
            return;
        }

        if (this.prev == -1) {
            this.prev = client.player.getInventory().selectedSlot;
        }
        client.player.getInventory().selectedSlot = weaponSlot;
        this.swap = true;
        this.ticks = this.holdTicks.getValue();
    }

    @Override
    public void onWTapPickTarget(ExtensionPickTargetEvent event) {
        if (event.getTarget() != null)
            this.select();
    }

}
