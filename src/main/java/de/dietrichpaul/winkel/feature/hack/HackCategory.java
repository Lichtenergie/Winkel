package de.dietrichpaul.winkel.feature.hack;

public enum HackCategory {

    BLOCK("block"),
    COMBAT("combat"),
    EXPLOIT("exploit"),
    FUN("fun"),
    MOVEMENT("movement"),
    PLAYER("player"),
    VISUAL("visual");

    private final String identifier;

    HackCategory(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

}
