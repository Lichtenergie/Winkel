package de.dietrichpaul.winkel.injection.accessor.item;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;

public interface IMiningToolItemMixin {
    TagKey<Block> getEffectedBlocks();
}
