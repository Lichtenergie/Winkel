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
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Button;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Scroll;
import de.dietrichpaul.winkel.property.AbstractProperty;
import de.dietrichpaul.winkel.util.MathUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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

    public FloatProperty(String name, String lowerCamelCase, String description, float value, float divisor) {
        this(name, lowerCamelCase, description, value, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, divisor);
    }

    @Override
    public void setValue(Float value) {
        super.setValue(MathUtil.setPrecision(MathHelper.clamp(Math.round(value / divisor) * divisor, this.min, this.max), this.getDecimalPrecision()));
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

    public int getDecimalPrecision() {
        float div = this.divisor;
        int counter = 0;
        while (div < 1) {
            div *= 10;
            counter++;
        }
        return counter;
    }

    @Override
    public void makeCommand(SimpleArgumentBuilder<InternalCommandSource, ?> builder) {
        builder.then(Command.argument("float", FloatArgumentType.floatArg(min, max)).executes(context -> {
            setValue(FloatArgumentType.getFloat(context, "float"));
            WinkelClient.INSTANCE.getChat().print("command.property.set", new LiteralText(getName()).formatted(Formatting.GRAY), new LiteralText(getParent().asString()).formatted(Formatting.GRAY), new LiteralText("").append(getValueText()).formatted(Formatting.GRAY));
            return 1;
        }));
    }

    public float getDivisor() {
        return divisor;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    @Override
    public Item createTabGuiItem() {
        Container container = new Container(() -> new LiteralText(this.getName()));
        container.add(new Scroll(this::getValue, this::getDivisor, this::getMin, this::getMax, this::getDecimalPrecision, this::setValue));

        return container;
    }

}
