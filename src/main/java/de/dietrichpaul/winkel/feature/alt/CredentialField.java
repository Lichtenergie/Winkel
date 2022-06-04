package de.dietrichpaul.winkel.feature.alt;

public class CredentialField {

    private String name;
    private boolean withheld;

    public CredentialField(String name, boolean withheld) {
        this.name = name;
        this.withheld = withheld;
    }

    public String getName() {
        return name;
    }

    public boolean isWithheld() {
        return withheld;
    }

}
