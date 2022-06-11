package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import de.dietrichpaul.winkel.property.AbstractProperty;
import de.dietrichpaul.winkel.util.HSBColor;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;

public class ColorProperty extends AbstractProperty<HSBColor> {

    private final boolean hasAlpha;

    public ColorProperty(String name, String lowerCamelCase, String description, HSBColor value, boolean hasAlpha) {
        super(name,lowerCamelCase, description, value);
        this.hasAlpha = hasAlpha;
    }

    @Override
    public void parse(String literal) {
        setValue(new HSBColor(Color.decode(literal)));
    }

    @Override
    public void readFromJson(JsonObject section) {
        float hue = section.has("hue") ? section.get("hue").getAsFloat() : getResetValue().getHue();
        float saturation = section.has("saturation") ? section.get("saturation").getAsFloat() : getResetValue().getSaturation();
        float brightness = section.has("brightness") ? section.get("brightness").getAsFloat() : getResetValue().getBrightness();
        int alpha = 255;
        if (this.hasAlpha) {
            alpha = section.has("alpha") ? section.get("alpha").getAsInt() : getResetValue().getAlpha();
        }
        setValue(new HSBColor(hue, saturation, brightness, alpha));
    }

    @Override
    public void writeToJson(JsonObject section) {
        HSBColor color = getValue();
        section.addProperty("hue", color.getHue());
        section.addProperty("saturation", color.getSaturation());
        section.addProperty("brightness", color.getBrightness());
        if (this.hasAlpha) {
            section.addProperty("alpha", color.getAlpha());
        }
    }

    @Override
    public Text getValueText() {
        Color color = new Color(getValue().getRGB(), true);

        String alphaHex = Integer.toHexString(color.getAlpha());
        if (alphaHex.length() != 2)
            alphaHex = "0" + alphaHex;
        String redHex = Integer.toHexString(color.getRed());
        if (redHex.length() != 2)
            redHex = "0" + redHex;
        String greenHex = Integer.toHexString(color.getGreen());
        if (greenHex.length() != 2)
            greenHex = "0" + greenHex;
        String blueHex = Integer.toHexString(color.getBlue());
        if (blueHex.length() != 2)
            blueHex = "0" + blueHex;

        MutableText detail = Text.literal("");
        detail.append(Text.literal("Red: " + color.getRed() + "\n")
                .formatted(Formatting.RED));
        detail.append(Text.literal("Green: " + color.getGreen() + "\n")
                .formatted(Formatting.GREEN));
        detail.append(Text.literal("Blue: " + color.getBlue() + "\n")
                .formatted(Formatting.BLUE));
        detail.append(Text.literal("Alpha: " + color.getAlpha())
                .formatted(Formatting.GRAY));

        MutableText literal = Text.literal("#" + alphaHex + redHex + greenHex + blueHex)
                .styled(style -> style
                        .withColor(getValue().getRGB())
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, detail))
                );

        return Text.literal("")
                .append(literal);
    }

    public boolean hasAlpha() {
        return this.hasAlpha;
    }

}
