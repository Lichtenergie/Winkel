package de.dietrichpaul.winkel.config.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.config.ConfigType;
import de.dietrichpaul.winkel.config.extension.json.JsonObjectConfig;

import java.util.List;
import java.util.Map;

public class MacroConfig extends JsonObjectConfig {

    public MacroConfig() {
        super("macros.json", ConfigType.PRE);
    }

    @Override
    public void read(JsonObject element) {
        for (Map.Entry<String, JsonElement> entry : element.entrySet()) {
            if (!entry.getValue().isJsonArray())
                continue;

            for (JsonElement action : entry.getValue().getAsJsonArray()) {
                winkel.getMacroList().bind(entry.getKey(), action.getAsString());
            }
        }
    }

    @Override
    public void write(JsonObject element) {
        for (Map.Entry<String, List<String>> entry : WinkelClient.INSTANCE.getMacroList().getActions().entrySet()) {
            JsonArray actions = new JsonArray();

            for (String action : entry.getValue())
                actions.add(action);

            element.add(entry.getKey(), actions);
        }
    }

}
