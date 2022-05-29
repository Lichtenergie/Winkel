package de.dietrichpaul.winkel.property;

import net.minecraft.util.StringIdentifiable;

import java.util.*;

public class PropertyMap {

    private final Map<StringIdentifiable, Set<AbstractProperty<?>>> properties = new LinkedHashMap<>();

    public void register(StringIdentifiable parent, AbstractProperty<?> property) {
        property.setup(parent);
        this.properties.computeIfAbsent(parent, k -> new LinkedHashSet<>()).add(property);
    }

    public AbstractProperty<?> getProperty(StringIdentifiable parent, String name) {
        Set<AbstractProperty<?>> properties = this.properties.get(parent);
        for (AbstractProperty<?> property : properties) {
            if (property.getName().equalsIgnoreCase(name)) return property;
        }
        return null;
    }

    public Set<AbstractProperty<?>> getPropertiesByParent(StringIdentifiable parent) {
        return this.properties.getOrDefault(parent, Collections.emptySet());
    }

    public Map<StringIdentifiable, Set<AbstractProperty<?>>> getProperties() {
        return properties;
    }

}
