package de.dietrichpaul.winkel.util;

import de.dietrichpaul.winkel.injection.accessor.item.IMiningToolItemMixin;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;

public class ItemUtils {
    @SuppressWarnings("rawtypes")
    public static TagKey[] TOOL_TYPES = {
            BlockTags.AXE_MINEABLE,
            BlockTags.HOE_MINEABLE,
            BlockTags.PICKAXE_MINEABLE,
            BlockTags.SHOVEL_MINEABLE
    };

    public static int getToolTypeId(ItemStack stack) {
        if (stack.getItem() instanceof MiningToolItem miningTool) {
            for (int i = 0; i < TOOL_TYPES.length; i++) {
                if (((IMiningToolItemMixin) miningTool).getEffectedBlocks() == TOOL_TYPES[i]) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static float getArmorValue(PlayerEntity player, ItemStack stack) {
        ArmorItem armor = (ArmorItem) stack.getItem();
        return 5 * armor.getProtection() + 3 * Enchantments.PROTECTION.getProtectionAmount(EnchantmentHelper.getLevel(Enchantments.PROTECTION, stack), DamageSource.player(player)) + armor.getToughness() + armor.getMaterial().getProtectionAmount(EquipmentSlot.LEGS);
    }

    public static float getWeaponValue(boolean legacy, PlayerEntity player, ItemStack stack) {
        float damage = 0;
        float speed = 1;
        float fire = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) * 0.1F;

        if (legacy) {
            String[] location = stack.getItem().getTranslationKey().split("\\.");
            damage = LegacyMaterial.getByLocation(location[location.length - 1]).damage;
        } else {
            if (stack.getItem() instanceof SwordItem sword) {
                damage = sword.getAttackDamage();
            } else if (stack.getItem() instanceof MiningToolItem miningTool) {
                damage = miningTool.getAttackDamage();
            }
        }
        damage += EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) * 1.25F;

        if (!legacy) {
            double attrSpeed = player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
            for (EntityAttributeModifier modifier : stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_SPEED)) {
                attrSpeed += modifier.getValue();
            }
            speed = (float) attrSpeed;
        }

        return damage * speed + fire;
    }

    public static float getBowValue(ItemStack stack) {
        float points = 1;
        points += EnchantmentHelper.getLevel(Enchantments.POWER, stack) * 3F;
        points += EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) * 2F;
        points += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
        return points;
    }

    public static float getCrossbowValue(ItemStack stack) {
        float points = 1;
        points += EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack) * 2F;
        points += EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) * 2F;
        points += EnchantmentHelper.getLevel(Enchantments.PIERCING, stack) * 1.5F;
        points += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
        points += EnchantmentHelper.getLevel(Enchantments.MENDING, stack) * 1.5F;
        points += EnchantmentHelper.getLevel(Enchantments.VANISHING_CURSE, stack) * 0.2F;
        return points;
    }

    public static void getBestBow(PlayerEntity player, int[] slot, float[] valueBuf) {
        slot[0] = -1;
        valueBuf[0] = Float.MIN_VALUE;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);

            if (!(stack.getItem() instanceof BowItem)) continue;

            float value = getBowValue(stack);

            if (valueBuf[0] >= value) continue;

            valueBuf[0] = value;
            slot[0] = i;
        }
    }

    public static void getBestCrossbow(PlayerEntity player, int[] slot, float[] valueBuf) {
        slot[0] = -1;
        valueBuf[0] = Float.MIN_VALUE;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);

            if (!(stack.getItem() instanceof CrossbowItem)) continue;

            float value = getCrossbowValue(stack);

            if (valueBuf[0] >= value) continue;

            valueBuf[0] = value;
            slot[0] = i;
        }
    }

    public static void getBestWeapon(boolean legacy, PlayerEntity player, int[] slot, float[] valueBuf) {
        slot[0] = -1;
        valueBuf[0] = Float.MIN_VALUE;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);

            if (!(stack.getItem() instanceof SwordItem) && !(stack.getItem() instanceof MiningToolItem)) continue;

            float value = getWeaponValue(legacy, player, stack);

            if (valueBuf[0] >= value) continue;

            slot[0] = i;
            valueBuf[0] = value;
        }
    }

    public static float getToolValue(ItemStack stack) {
        float miningSpeed = ((MiningToolItem) stack.getItem()).getMaterial().getDurability();

        if (miningSpeed > 1.0f) {
            int i = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);
            if (i > 0) {
                miningSpeed += (float) (i * i + 1);
            }
        }

        return miningSpeed;
    }

    public static float getShieldValue(ItemStack stack) {
        float points = 1;
        points += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 3F;
        points += EnchantmentHelper.getLevel(Enchantments.MENDING, stack) * 2F;
        points += EnchantmentHelper.getLevel(Enchantments.VANISHING_CURSE, stack);
        return points;
    }

    public static void getBestShield(PlayerEntity player, int[] slot, float[] valueBuf) {
        slot[0] = -1;
        valueBuf[0] = Float.MIN_VALUE;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);

            if (!(stack.getItem() instanceof ShieldItem)) continue;

            float value = getShieldValue(stack);

            if (valueBuf[0] >= value) continue;

            valueBuf[0] = value;
            slot[0] = i;
        }
    }

    public static float getTridentValue(ItemStack stack) {
        float points = 1;
        points += EnchantmentHelper.getLevel(Enchantments.LOYALTY, stack) * 4F;
        points += EnchantmentHelper.getLevel(Enchantments.CHANNELING, stack) * 3.5F;
        points += EnchantmentHelper.getLevel(Enchantments.RIPTIDE, stack) * 2F;
        points += EnchantmentHelper.getLevel(Enchantments.IMPALING, stack) * 5F;
        points += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 3F;
        points += EnchantmentHelper.getLevel(Enchantments.MENDING, stack) * 2F;
        points += EnchantmentHelper.getLevel(Enchantments.VANISHING_CURSE, stack) * 0.25F;
        return points;
    }

    public static void getBestTrident(PlayerEntity player, int[] slot, float[] valueBuf) {
        slot[0] = -1;
        valueBuf[0] = Float.MIN_VALUE;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);

            if (!(stack.getItem() instanceof TridentItem)) continue;

            float value = getTridentValue(stack);

            if (valueBuf[0] >= value) continue;

            valueBuf[0] = value;
            slot[0] = i;
        }
    }

    public static void getBestTools(PlayerEntity player, int[] slots, float[] values) {
        for (int i = 0; i < TOOL_TYPES.length; i++) {
            slots[i] = -1;
            values[i] = 0;
        }
        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);

            if (!(stack.getItem() instanceof MiningToolItem)) continue;

            int id = getToolTypeId(stack);
            float value = getToolValue(stack);

            if (values[id] >= value) continue;

            values[id] = value;
            slots[id] = i;
        }
    }

    public static void getBestArmor(PlayerEntity player, int[] slots, float[] values) {
        for (int i = 0; i < 4; i++) {
            slots[i] = -1;
            values[i] = Float.MIN_VALUE;

            ItemStack stack = player.getInventory().getArmorStack(i);

            if (!(stack.getItem() instanceof ArmorItem)) continue;

            values[i] = getArmorValue(player, stack);
        }
        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);

            if (!(stack.getItem() instanceof ArmorItem)) continue;

            float value = getArmorValue(player, stack);
            int slot = ((ArmorItem) stack.getItem()).getSlotType().getEntitySlotId();

            if (value <= values[slot]) continue;

            slots[slot] = i;
            values[slot] = value;
        }
    }
}
