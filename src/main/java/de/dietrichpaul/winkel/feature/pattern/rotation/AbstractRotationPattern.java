package de.dietrichpaul.winkel.feature.pattern.rotation;

public abstract class AbstractRotationPattern implements RotationPattern {

    private final String name;

    public AbstractRotationPattern(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

}
