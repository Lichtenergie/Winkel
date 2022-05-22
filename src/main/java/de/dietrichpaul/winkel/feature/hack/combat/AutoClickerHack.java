package de.dietrichpaul.winkel.feature.hack.combat;

import de.dietrichpaul.winkel.event.list.UpdateListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.util.Timer;

public class AutoClickerHack extends Hack implements UpdateListener {

    public AutoClickerHack() {
        super("AutoClicker", "", HackCategory.COMBAT);
    }

    private Timer timer;

    @Override
    public void onUpdate() {

    }

}
