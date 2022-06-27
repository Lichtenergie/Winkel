package de.dietrichpaul.winkel.feature.hack;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.hack.impl.block.FastPlaceHack;
import de.dietrichpaul.winkel.feature.hack.impl.combat.AimbotHack;
import de.dietrichpaul.winkel.feature.hack.impl.combat.KillauraHack;
import de.dietrichpaul.winkel.feature.hack.impl.fun.HealthSpoofHack;
import de.dietrichpaul.winkel.feature.hack.impl.item.AutoSwordHack;
import de.dietrichpaul.winkel.feature.hack.impl.movement.*;
import de.dietrichpaul.winkel.feature.hack.impl.visual.FullBrightHack;
import de.dietrichpaul.winkel.feature.hack.impl.visual.HudHack;
import org.checkerframework.checker.units.qual.K;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class HackList {

    private Map<String, Hack> hacks = new TreeMap<>(String::compareToIgnoreCase);

    // Block
    public FastPlaceHack fastPlace = new FastPlaceHack();

    // Combat
    public AimbotHack aimbot = new AimbotHack();
    public KillauraHack killaura = new KillauraHack();

    // Fun
    public HealthSpoofHack healthSpoof = new HealthSpoofHack();

    // Item
    public AutoSwordHack autoSword = new AutoSwordHack();

    // Movement
    public JetpackHack jetpackHack = new JetpackHack();
    public NoFallHack noFallHack = new NoFallHack();
    public SpiderHack spider = new SpiderHack();
    public SprintHack sprint = new SprintHack();
    public StepHack step = new StepHack();
    public WTapHack wtap = new WTapHack();

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
        hack.makeProperties(WinkelClient.INSTANCE.getPropertyMap());
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
