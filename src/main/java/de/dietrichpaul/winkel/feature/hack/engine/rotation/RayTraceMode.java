package de.dietrichpaul.winkel.feature.hack.engine.rotation;

import de.dietrichpaul.winkel.util.EnumIdentifiable;

public enum RayTraceMode implements EnumIdentifiable {

    LEGIT("Legit", "legit"),
    FORCE_TARGET("Force target", "forceTarget");

    private String display;
    private String lowerCamelCase;

    RayTraceMode(String display, String lowerCamelCase) {
        this.display = display;
        this.lowerCamelCase = lowerCamelCase;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public String getLowerCamelCase() {
        return lowerCamelCase;
    }

}
