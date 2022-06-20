package de.dietrichpaul.winkel.feature.hack.engine.rotation;

import de.dietrichpaul.winkel.util.EnumIdentifiable;

public enum StrafeMode implements EnumIdentifiable {

    NONE("None", "none",false, false, false),
    FORCE("Force", "force", true, false, false),
    SILENT("Silent", "silent",true, false, true),
    MOON_WALK("Moon walk", "moonWalk", true, false, true),
    COMBINED_SPRINT("Combined sprint", "combinedSprint", true, true, true);

    private String display;
    private String lowerCamelCase;

    boolean fix;
    boolean sprint;
    boolean silentAngle;

    StrafeMode(String display, String lowerCamelCase, boolean fix, boolean sprint, boolean silentAngle) {
        this.display = display;
        this.lowerCamelCase = lowerCamelCase;
        this.fix = fix;
        this.sprint = sprint;
        this.silentAngle = silentAngle;
    }

    @Override
    public String getDisplay() {
        return this.display;
    }

    @Override
    public String getLowerCamelCase() {
        return this.lowerCamelCase;
    }

}
