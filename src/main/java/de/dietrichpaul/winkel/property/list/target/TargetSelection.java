package de.dietrichpaul.winkel.property.list.target;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public record TargetSelection(MinecraftClient client, Entity entity) {
}
