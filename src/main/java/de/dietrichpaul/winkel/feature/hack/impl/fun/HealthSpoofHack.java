package de.dietrichpaul.winkel.feature.hack.impl.fun;

import de.dietrichpaul.winkel.event.list.render.HealthListener;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackCategory;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.EnumProperty;
import de.dietrichpaul.winkel.property.list.IntegerProperty;
import de.dietrichpaul.winkel.util.EnumIdentifiable;
import net.minecraft.entity.attribute.EntityAttributes;

public class HealthSpoofHack extends Hack implements HealthListener {

    public HealthSpoofHack() {
        super("HealthSpoof", "Legit Godmode", HackCategory.FUN);
    }

    private EnumProperty<HealthSpoofMode> modeProperty = new EnumProperty<>("Mode", "mode", "", HealthSpoofMode.PERCENTAGE, HealthSpoofMode.values());
    private IntegerProperty valueProperty = new IntegerProperty("Value", "value", "", 100, 0, 100);

    @Override
    protected void makeProperties(PropertyMap map) {
        addProperty(map, this.modeProperty);
        addProperty(map, this.valueProperty);
        super.makeProperties(map);
    }

    @Override
    protected void onEnable() {
        events.subscribe(HealthListener.class, this);
    }

    @Override
    protected void onDisable() {
        events.unsubscribe(HealthListener.class, this);
    }

    @Override
    public void onHealth(HealthEvent event) {
        if (modeProperty.getValue() == HealthSpoofMode.STATIC) {
            event.setHealth(valueProperty.getValue());
        } else if (modeProperty.getValue() == HealthSpoofMode.PERCENTAGE) {
            event.setHealth((float) (valueProperty.getValue() * client.player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH)) / 100F);
        } else if (modeProperty.getValue() == HealthSpoofMode.ANIMATION) {
            double percentage = (Math.sin(System.currentTimeMillis() / 100D) + 1) / 2;
            event.setHealth((float) (percentage * client.player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH)));
        }
    }

    enum HealthSpoofMode implements EnumIdentifiable {

        STATIC("Static", "static"),
        PERCENTAGE("Percentage", "percentage"),
        ANIMATION("Animation", "animation");

        private String display;
        private String lowerCamelCase;

        HealthSpoofMode(String display, String lowerCamelCase) {
            this.display = display;
            this.lowerCamelCase = lowerCamelCase;
        }

        @Override
        public String getDisplay() {
            return this.display;
        }

        @Override
        public String getLowerCamelCase() {
            return this.lowerCamelCase;
        }

    }

}
