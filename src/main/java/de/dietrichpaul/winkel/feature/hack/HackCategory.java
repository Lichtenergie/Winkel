package de.dietrichpaul.winkel.feature.hack;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public enum HackCategory {

    BLOCK("block"),
    COMBAT("combat"),
    EXPLOIT("exploit"),
    FUN("fun"),
    MOVEMENT("movement"),
    PLAYER("player"),
    VISUAL("visual");

    private final String identifier;
    private final Text display;

    HackCategory(String identifier) {
        this.identifier = identifier;
        this.display = new TranslatableText("category." + identifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    public Text getDisplay() {
        return display;
    }

}
