package de.dietrichpaul.winkel.property.list;

import com.google.gson.JsonObject;
import de.dietrichpaul.winkel.feature.gui.tab.Item;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Container;
import de.dietrichpaul.winkel.feature.gui.tab.impl.Radio;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ModeProperty<T extends StringIdentifiable> extends AbstractProperty<T> {

    private final List<T> modes;

    public ModeProperty(String name, String lowerCamelCase, String description, int index, Collection<T> modes) {
        this(name, lowerCamelCase, description, index, new LinkedList<>(modes));
    }


    public ModeProperty(String name, String lowerCamelCase, String description, int index, List<T> modes) {
        super(name, lowerCamelCase, description, modes.get(index));
        this.modes = modes;
    }

    @Override
    public void parse(String literal) {

    }

    @Override
    public void readFromJson(JsonObject element) {

    }

    @Override
    public void writeToJson(JsonObject element) {

    }

    @Override
    public Item createTabGuiItem() {
        Container container = new Container(() -> Text.literal(this.getName()));
        for (T t : this.modes) {
            container.add(new Radio(() -> Text.literal(t.asString()), () -> this.getValue() == t, () -> {
                if (this.getValue() != t)
                    this.setValue(t);
            }));
        }
        return container;
    }

}
