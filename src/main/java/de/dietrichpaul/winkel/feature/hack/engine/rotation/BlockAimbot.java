package de.dietrichpaul.winkel.feature.hack.engine.rotation;

import de.dietrichpaul.winkel.feature.hack.HackCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public abstract class BlockAimbot extends SpoofAimbot {

    public BlockAimbot(String name, String description, HackCategory category) {
        super(name, description, category);
    }

    protected BlockHitResult target;

    public abstract BlockHitResult searchTarget();

    @Override
    public HitResult getTarget() {
        return this.target;
    }

    @Override
    public boolean hasTarget() {
        return this.target != null;
    }

    @Override
    public void invalidateTarget() {
        this.target = null;
    }

    @Override
    public void pickTarget() {
        this.target = searchTarget();
    }

}
