package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public class FloatProperty extends AbstractProperty<Float> {

    private final float min;
    private final float max;
    private final float divisor;

    public FloatProperty(String name, String lowerCamelCase, String description, float value, float min, float max, float divisor) {
        super(name, lowerCamelCase, description, value);
        this.min = min;
        this.max = max;
        this.divisor = divisor;
    }

    @Override
    public void setValue(Float value) {
        super.setValue(MathHelper.clamp(Math.round(value / divisor) * divisor, this.min, this.max));
    }

    @Override
    public void parse(String literal) {
        setValue(Float.parseFloat(literal));
    }

    @Override
    public void readFromJson(JsonObject element) {
        setValue(element.has("value") ? element.get("value").getAsFloat() : getResetValue());
    }

    @Override
    public void writeToJson(JsonObject element) {
        element.addProperty("value", getValue());
    }

    @Override
    public void makeCommand(SimpleArgumentBuilder<InternalCommandSource, ?> builder) {
        builder.then(Command.argument("float", FloatArgumentType.floatArg(min, max)).executes(context -> {
            setValue(FloatArgumentType.getFloat(context, "float"));
            WinkelClient.INSTANCE.getChat().print("command.property.set", new LiteralText(getName()).formatted(Formatting.GRAY), new LiteralText(getParent().asString()).formatted(Formatting.GRAY), new LiteralText("").append(getValueText()).formatted(Formatting.GRAY));
            return 1;
        }));
    }

}
