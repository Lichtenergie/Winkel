package de.dietrichpaul.winkel.property.list.target;

import net.minecraft.entity.Entity;

import java.util.function.Predicate;

@FunctionalInterface
public interface IndependentFilter extends Predicate<Entity> {

}
