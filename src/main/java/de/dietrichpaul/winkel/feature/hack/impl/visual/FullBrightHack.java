package de.dietrichpaul.winkel.feature.hack.impl.visual;

import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;

public class FullBrightHack extends Hack {

    public FullBrightHack() {
        super("FullBright", "", HackCategory.VISUAL);
    }

    private float strength = 2.0F;

    public float getStrength() {
        return .65f;
    }

}
