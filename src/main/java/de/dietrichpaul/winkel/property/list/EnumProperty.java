package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.arguments.EnumArgument;
import de.dietrichpaul.winkel.property.AbstractProperty;
import de.dietrichpaul.winkel.util.EnumIdentifiable;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EnumProperty<T extends Enum<T> & EnumIdentifiable> extends AbstractProperty<T> {

    private final T[] values;

    public EnumProperty(String name, String lowerCamelCase, String description, T value, T[] values) {
        super(name, lowerCamelCase, description, value);
        this.values = values;
    }

    @Override
    public void parse(String literal) {
        for (T t : this.values) {
            if (t.name().equals(literal)) {
                setValue(t);
                break;
            }
        }
    }

    @Override
    public LiteralArgumentBuilder<InternalCommandSource> makeCommand(LiteralArgumentBuilder<InternalCommandSource> builder) {
        return builder.then(Command.argument("enum", EnumArgument.enumArgument(this.values)).executes(context -> {
            setValue(EnumArgument.getEnumConstant(context, "enum"));
            WinkelClient.INSTANCE.getChat().print("command.property.set", new LiteralText(getName()).formatted(Formatting.GRAY), new LiteralText(getParent().asString()).formatted(Formatting.GRAY), new LiteralText("").append(getValueText()).formatted(Formatting.GRAY));
            return 1;
        }));
    }

    @Override
    public Text getValueText() {
        return Text.of(this.getValue().getDisplay());
    }

    @Override
    public void readFromJson(JsonObject element) {
        if (element.has("enum")) parse(element.get("enum").getAsString());
    }

    @Override
    public void writeToJson(JsonObject element) {
        element.addProperty("enum", getValue().name());
    }

}