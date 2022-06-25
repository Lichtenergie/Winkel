package de.dietrichpaul.winkel.feature.hack.engine.click;

public interface InputHandler {

    boolean canClick();

    int getClickingPriority();

    void click(ClickCallback callback);

}
