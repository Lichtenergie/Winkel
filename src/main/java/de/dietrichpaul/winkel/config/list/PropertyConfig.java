package de.dietrichpaul.winkel.config.list;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dietrichpaul.winkel.config.ConfigType;
import de.dietrichpaul.winkel.config.extension.json.JsonObjectConfig;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;
import java.util.Set;

public class PropertyConfig extends JsonObjectConfig {

    public PropertyConfig() {
        super("properties.json", ConfigType.PRE);
    }


    @Override
    public void read(JsonObject element) {
        for (Map.Entry<StringIdentifiable, Set<AbstractProperty<?>>> assignment : winkel.getPropertyMap().getProperties().entrySet()) {
            String parent = assignment.getKey().asString();

            if (element.has(parent) && element.get(parent).isJsonObject()) {
                JsonObject properties = element.getAsJsonObject(parent);

                for (AbstractProperty<?> property : assignment.getValue()) {
                    JsonElement propertyElement = properties.get(property.getName());
                    if (propertyElement != null && propertyElement.isJsonObject()) {
                        property.readFromJson(propertyElement.getAsJsonObject());
                    }
                }
            }
        }
    }

    @Override
    public void write(JsonObject element) {
        for (Map.Entry<StringIdentifiable, Set<AbstractProperty<?>>> assignment : winkel.getPropertyMap().getProperties().entrySet()) {
            String parent = assignment.getKey().asString();

            JsonObject parentObject = new JsonObject();

            for (AbstractProperty<?> property : assignment.getValue()) {
                JsonObject propertyObject = new JsonObject();
                property.writeToJson(propertyObject);

                parentObject.add(property.getName(), propertyObject);
            }

            element.add(parent, parentObject);
        }
    }

}
