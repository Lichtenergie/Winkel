package de.dietrichpaul.winkel.feature.hack.impl.item;

import de.dietrichpaul.winkel.event.list.ExtensionPickTargetListener;
import de.dietrichpaul.winkel.event.list.InputHandleListener;
import de.dietrichpaul.winkel.event.list.raytrace.PostRayTraceListener;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.BooleanProperty;
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
    private final BooleanProperty swapBack = new BooleanProperty("SwapBack", "swapBack","", false);

    private int prev;

    @Override
    protected void onEnable() {
        events.subscribe(InputHandleListener.class, this);
        events.subscribe(ExtensionPickTargetListener.class, this);
    }

    @Override
    protected void onDisable() {
        events.unsubscribe(InputHandleListener.class, this);
        events.unsubscribe(ExtensionPickTargetListener.class, this);
        if (this.prev != -1 && this.swapBack.getValue()) {
            client.player.getInventory().selectedSlot = this.prev;
        }
    }

    @Override
    protected void makeProperties(PropertyMap map) {
        addProperty(map, this.legacy);
        addProperty(map, this.swapBack);
        super.makeProperties(map);
    }

    @Override
    public void onHandleInput(InputHandleEvent event) {
        int prev = this.prev;
        if (client.crosshairTarget instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity) {
            this.select();
        } else {
            this.prev = -1;
        }
        if (this.swapBack.getValue() && prev != -1 && this.prev == -1) {
            client.player.getInventory().selectedSlot = prev;
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
            this.prev = -1;
            return;
        }

        if (this.prev == -1) {
            this.prev = client.player.getInventory().selectedSlot;
        }
        client.player.getInventory().selectedSlot = weaponSlot;
    }

    @Override
    public void onWTapPickTarget(ExtensionPickTargetEvent event) {
        if (event.getTarget() != null)
            this.select();
    }

}
