package de.dietrichpaul.winkel.util.priority.killaura;


import de.dietrichpaul.winkel.util.EnumIdentifiable;
import de.dietrichpaul.winkel.util.priority.killaura.algorithm.*;

public enum KillAuraPriorityMode implements EnumIdentifiable {

    ENTITY_ID("Entity ID", "entityId", new EntityIdTargetSortingAlgorithm()),
    DISTANCE("Closest", "closest", new ClosestTargetSortingAlgorithm()),
    HEALTH("Health", "health", new HealthTargetSortingAlgorithm()),
    SWITCH("Switch", "switch", new SwitchTargetSortingArgument()),
    DIRECTION("Direction", "direction", new DirectionTargetSortingAlgorithm());

    private String display;
    private String lowerCamelCase;

    private TargetSortingAlgorithm targetSortingAlgorithm;

    KillAuraPriorityMode(String display, String lowerCamelCase, TargetSortingAlgorithm targetSortingAlgorithm) {
        this.display = display;
        this.lowerCamelCase = lowerCamelCase;
        this.targetSortingAlgorithm = targetSortingAlgorithm;
    }

    public TargetSortingAlgorithm getTargetSortingAlgorithm() {
        return targetSortingAlgorithm;
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
