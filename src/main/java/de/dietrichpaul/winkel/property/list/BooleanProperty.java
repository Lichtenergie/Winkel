package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.BoolArgumentType;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Button;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.text.Text;
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
    public void makeCommand(SimpleArgumentBuilder<InternalCommandSource, ?> builder) {
        builder.then(Command.argument("boolean", BoolArgumentType.bool()).executes(context -> {
            setValue(BoolArgumentType.getBool(context, "boolean"));
            WinkelClient.INSTANCE.getChat().print("command.property.set", Text.literal(getName()).formatted(Formatting.GRAY), Text.literal(getParent().asString()).formatted(Formatting.GRAY), Text.literal("").append(getValueText()).formatted(Formatting.GRAY));
            return 1;
        }));
    }

    @Override
    public Item createTabGuiItem() {
        Container container = new Container(() -> Text.literal(this.getName()));
        container.add(new Button(() -> {
            Text text = Text.literal("Enable")
                    .formatted(Formatting.GREEN);
            if (this.getValue())
                text = Text.literal("→ ").append(text);
            return text;
        }, () -> this.setValue(true)));
        container.add(new Button(() -> {
            Text text = Text.literal("Disable")
                    .formatted(Formatting.RED);
            if (!this.getValue())
                text = Text.literal("→ ").append(text);
            return text;
        }, () -> this.setValue(false)));
        return container;
    }
}
