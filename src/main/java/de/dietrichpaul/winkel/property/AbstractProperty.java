package de.dietrichpaul.winkel.property;

import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

public abstract class AbstractProperty<T> {

    private final String name;
    private final String lowerCamelCase;
    private final String description;

    private StringIdentifiable parent;

    private T value;
    private final T resetValue;

    public AbstractProperty(String name, String lowerCamelCase, String description, T value) {
        this.name = name;
        this.lowerCamelCase = lowerCamelCase;
        this.description = description;
        this.value = value;
        this.resetValue = value;
    }

    public void setup(StringIdentifiable parent) {
        this.parent = parent;
    }

    public String getLowerCamelCase() {
        return lowerCamelCase;
    }

    public abstract void parse(String literal);

    public abstract void readFromJson(JsonObject element);

    public abstract void writeToJson(JsonObject element);

    public LiteralArgumentBuilder<InternalCommandSource> makeCommand(LiteralArgumentBuilder<InternalCommandSource> builder) {
        return null;
    }

    public StringIdentifiable getParent() {
        return parent;
    }

    public Text getValueText() {
        return new LiteralText(String.valueOf(getValue()));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getResetValue() {
        return resetValue;
    }

}