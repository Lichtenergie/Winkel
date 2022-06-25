package de.dietrichpaul.winkel.config.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.config.ConfigType;
import de.dietrichpaul.winkel.config.extension.json.JsonArrayConfig;
import de.dietrichpaul.winkel.feature.hack.Hack;

public class EnabledHacksConfig extends JsonArrayConfig {

    public EnabledHacksConfig() {
        super("enabled_hacks.json", ConfigType.IN_GAME);
    }

    @Override
    protected void read(JsonArray element) {
        for (JsonElement modState : element) {
            if (modState.isJsonPrimitive()) {
                Hack hack = winkel.getHackList().getHack(modState.getAsString());
                if (!hack.isEnabled())
                    hack.toggle();
            }
        }
    }

    @Override
    protected void write(JsonArray element) {
        for (Hack hack : winkel.getHackList().getHacks()) {
            if (hack.isEnabled())
                element.add(hack.asString());
        }
    }

}
