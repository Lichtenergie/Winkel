package de.dietrichpaul.winkel.config;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.config.list.EnabledHacksConfig;
import de.dietrichpaul.winkel.config.list.MacroConfig;
import de.dietrichpaul.winkel.event.list.UpdateListener;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

public class ConfigManager implements UpdateListener {

    public EnabledHacksConfig enabledHacks = new EnabledHacksConfig();
    public MacroConfig macro = new MacroConfig();
    private Set<AbstractConfig> configs = new LinkedHashSet<>();

    public ConfigManager() {
        for (Field field : getClass().getFields()) {
            if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                try {
                    addConfig((AbstractConfig) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addConfig(AbstractConfig config) {
        this.configs.add(config);
    }

    public void start() {
        WinkelClient.INSTANCE.getEventDispatcher().subscribe(UpdateListener.class, this);
        for (AbstractConfig config : this.configs) {
            if (config.getType() == ConfigType.PRE) {
                try {
                    config.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        for (AbstractConfig config : this.configs) {
            try {
                if (config.getType() == ConfigType.IN_GAME)
                    config.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WinkelClient.INSTANCE.getEventDispatcher().unsubscribe(UpdateListener.class, this);
    }


}
