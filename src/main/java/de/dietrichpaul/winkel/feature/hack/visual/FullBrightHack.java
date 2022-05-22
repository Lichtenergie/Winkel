package de.dietrichpaul.winkel.feature.hack.visual;

import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;

public class FullBrightHack extends Hack {

    public FullBrightHack() {
        super("FullBright", "", HackCategory.VISUAL);
    }

    private float strength = 1.0F;

    public float getStrength() {
        return this.strength;
    }

}
