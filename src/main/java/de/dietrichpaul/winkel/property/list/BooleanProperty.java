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
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Button;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
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
            WinkelClient.INSTANCE.getChat().print("command.property.set", new LiteralText(getName()).formatted(Formatting.GRAY), new LiteralText(getParent().asString()).formatted(Formatting.GRAY), new LiteralText("").append(getValueText()).formatted(Formatting.GRAY));
            return 1;
        }));
    }

    @Override
    public Item createTabGuiItem() {
        Container container = new Container(() -> new LiteralText(this.getName()));
        container.add(new Button(() -> {
            Text text = new LiteralText("Enable")
                    .formatted(Formatting.GREEN);
            if (this.getValue())
                text = new LiteralText("→ ").append(text);
            return text;
        }, () -> this.setValue(true)));
        container.add(new Button(() -> {
            Text text = new LiteralText("Disable")
                    .formatted(Formatting.RED);
            if (!this.getValue())
                text = new LiteralText("→ ").append(text);
            return text;
        }, () -> this.setValue(false)));
        return container;
    }

}
