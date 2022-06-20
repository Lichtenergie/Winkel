package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.arguments.EnumArgument;
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Radio;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Scroll;
import de.dietrichpaul.winkel.property.AbstractProperty;
import de.dietrichpaul.winkel.util.EnumIdentifiable;
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
    public void makeCommand(SimpleArgumentBuilder<InternalCommandSource, ?> builder) {
        builder.then(Command.argument("enum", EnumArgument.enumArgument(this.values)).executes(context -> {
            setValue(EnumArgument.getEnumConstant(context, "enum"));
            WinkelClient.INSTANCE.getChat().print("command.property.set", Text.literal(getName()).formatted(Formatting.GRAY), Text.literal(getParent().asString()).formatted(Formatting.GRAY), Text.literal("").append(getValueText()).formatted(Formatting.GRAY));
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

    @Override
    public Item createTabGuiItem() {
        Container container = new Container(() -> Text.literal(this.getName()));
        for (T t : this.values) {
            container.add(new Radio(() -> {
                return Text.literal(t.getDisplay());
            }, () -> {
                return this.getValue() == t;
            }, () -> {
                setValue(t);
            }));
        }

        return container;
    }

}
