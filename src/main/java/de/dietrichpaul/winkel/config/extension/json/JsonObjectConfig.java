package de.dietrichpaul.winkel.config.extension.json;

import com.google.gson.JsonObject;
import de.dietrichpaul.winkel.config.ConfigType;
import de.dietrichpaul.winkel.config.extension.JsonConfig;

public abstract class JsonObjectConfig extends JsonConfig<JsonObject> {

    public JsonObjectConfig(String name, ConfigType type) {
        super(name, type);
    }

    @Override
    protected JsonObject make() {
        return new JsonObject();
    }

}
