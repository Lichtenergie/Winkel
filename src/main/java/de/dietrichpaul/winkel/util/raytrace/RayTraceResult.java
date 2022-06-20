package de.dietrichpaul.winkel.util.raytrace;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;

public record RayTraceResult(HitResult collision, Entity targeted) {
}
