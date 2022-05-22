package de.dietrichpaul.winkel.injection.mixin.item;

import de.dietrichpaul.winkel.injection.accessor.item.IMiningToolItemMixin;
import net.minecraft.block.Block;
import net.minecraft.item.MiningToolItem;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin implements IMiningToolItemMixin {
    @Shadow @Final private TagKey<Block> effectiveBlocks;

    @Override
    public TagKey<Block> getEffectedBlocks() {
        return this.effectiveBlocks;
    }
}
