package de.dietrichpaul.winkel.property.list.target;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public record TargetFilter(String name, String lowerCamelCase, String description, BiPredicate<MinecraftClient, Entity> filter) {

}
