package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Scroll;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public class IntegerProperty extends AbstractProperty<Integer> {

    private final int min;
    private final int max;

    public IntegerProperty(String name, String lowerCamelCase, String description, int value, int min, int max) {
        super(name, lowerCamelCase, description, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public void setValue(Integer value) {
        super.setValue(MathHelper.clamp(value, this.min, this.max));
    }

    @Override
    public void parse(String literal) {
        setValue(Integer.parseInt(literal));
    }

    @Override
    public void readFromJson(JsonObject element) {
        setValue(element.has("value") ? element.get("value").getAsInt() : getResetValue());
    }

    @Override
    public void writeToJson(JsonObject element) {
        element.addProperty("value", getValue());
    }

    @Override
    public void makeCommand(SimpleArgumentBuilder<InternalCommandSource, ?> builder) {
         builder.then(Command.argument("integer", IntegerArgumentType.integer(min, max)).executes(context -> {
            setValue(IntegerArgumentType.getInteger(context, "integer"));
            WinkelClient.INSTANCE.getChat().print("command.property.set", Text.literal(getName()).formatted(Formatting.GRAY), Text.literal(getParent().asString()).formatted(Formatting.GRAY), Text.literal("").append(getValueText()).formatted(Formatting.GRAY));
            return 1;
        }));
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public Item createTabGuiItem() {
        Container container = new Container(() -> Text.literal(this.getName()));
        container.add(new Scroll(this::getValue, () -> 1, this::getMin, this::getMax, () -> 0, n -> setValue(n.intValue())));

        return container;
    }

}
