package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class BooleanProperty extends AbstractProperty<Boolean> {

    public BooleanProperty(String name, String lowerCamelCase, String description, boolean value) {
        super(name, lowerCamelCase, description, value);
    }

    @Override
    public void parse(String literal) {
        setValue(Boolean.parseBoolean(literal));
    }

    @Override
    public void readFromJson(JsonObject element) {
        setValue(element.has("value") ? element.get("value").getAsBoolean() : getResetValue());
    }

    @Override
    public void writeToJson(JsonObject element) {
        element.addProperty("value", getValue());
    }

    public void toggle() {
        setValue(!getValue());
    }

    @Override
    public LiteralArgumentBuilder<InternalCommandSource> makeCommand(LiteralArgumentBuilder<InternalCommandSource> builder) {
        return builder.then(Command.argument("boolean", BoolArgumentType.bool()).executes(context -> {
            setValue(BoolArgumentType.getBool(context, "boolean"));
            WinkelClient.INSTANCE.getChat().print("command.property.set", new LiteralText(getName()).formatted(Formatting.GRAY), new LiteralText(getParent().asString()).formatted(Formatting.GRAY), new LiteralText("").append(getValueText()).formatted(Formatting.GRAY));
            return 1;
        }));
    }

}
