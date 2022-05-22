package de.dietrichpaul.winkel.feature.hack;

import de.dietrichpaul.winkel.feature.hack.block.FastPlaceHack;
import de.dietrichpaul.winkel.feature.hack.movement.JetpackHack;
import de.dietrichpaul.winkel.feature.hack.movement.SpiderHack;
import de.dietrichpaul.winkel.feature.hack.movement.SprintHack;
import de.dietrichpaul.winkel.feature.hack.movement.StepHack;
import de.dietrichpaul.winkel.feature.hack.visual.FullBrightHack;
import de.dietrichpaul.winkel.feature.hack.visual.HudHack;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class HackList {

    private Map<String, Hack> hacks = new TreeMap<>(String::compareToIgnoreCase);

    // Block
    public FastPlaceHack fastPlace = new FastPlaceHack();

    // Movement
    public JetpackHack jetpackHack = new JetpackHack();
    public SpiderHack spider = new SpiderHack();
    public SprintHack sprint = new SprintHack();
    public StepHack step = new StepHack();

    // Visual
    public FullBrightHack fullBright = new FullBrightHack();
    public HudHack hud = new HudHack();

    public HackList() {
        for (Field field : getClass().getFields()) {
            if (Hack.class.isAssignableFrom(field.getType())) {
                try {
                    addHack((Hack) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addHack(Hack hack) {
        this.hacks.put(hack.getName(), hack);
    }

    public Hack getHack(String name) {
        return hacks.get(name);
    }

    public <T extends Hack> T getHack(Class<T> clazz) {
        for (Hack hack : hacks.values()) {
            if (hack.getClass() == clazz) {
                return (T) hack;
            }
        }
        return null;
    }

    public Collection<Hack> getHacks() {
        return hacks.values();
    }

    public Collection<String> getHackNames() {
        return hacks.keySet();
    }

}
