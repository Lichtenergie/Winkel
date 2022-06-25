package de.dietrichpaul.winkel.config.extension.json;

import com.google.gson.JsonArray;
import de.dietrichpaul.winkel.config.ConfigType;
import de.dietrichpaul.winkel.config.extension.JsonConfig;

public abstract class JsonArrayConfig extends JsonConfig<JsonArray> {

    public JsonArrayConfig(String name, ConfigType type) {
        super(name, type);
    }

    @Override
    protected JsonArray make() {
        return new JsonArray();
    }

}
