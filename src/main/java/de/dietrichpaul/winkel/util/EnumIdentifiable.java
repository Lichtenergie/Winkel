package de.dietrichpaul.winkel.util;

import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

public interface EnumIdentifiable extends StringIdentifiable {

    String getDisplay();

    String getLowerCamelCase();

    @Override
    default String asString() {
        return getLowerCamelCase();
    }

}
