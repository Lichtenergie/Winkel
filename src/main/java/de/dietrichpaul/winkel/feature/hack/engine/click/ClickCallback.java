package de.dietrichpaul.winkel.feature.hack.engine.click;

public interface ClickCallback {

    void pressAttack(int times);

    void pressUse(int times);

    default void pressAttack() {
        this.pressAttack(1);
    }

    default void pressUse() {
        this.pressUse(1);
    }

}
