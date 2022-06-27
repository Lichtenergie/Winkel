package de.dietrichpaul.winkel.feature.hack;

import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.EventDispatcher;
import de.dietrichpaul.winkel.feature.Chat;
import de.dietrichpaul.winkel.property.AbstractProperty;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.property.list.BooleanProperty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

public class Hack implements StringIdentifiable {

    private String name;
    private String description;
    private HackCategory category;

    protected MinecraftClient client = MinecraftClient.getInstance();
    protected WinkelClient winkel = WinkelClient.INSTANCE;
    protected EventDispatcher events = winkel.getEventDispatcher();
    protected Chat chat = winkel.getChat();

    private boolean enabled;

    private BooleanProperty disableOnDeath = new BooleanProperty("DisableOnDeath", "disableOnDeath", "", false);

    public Hack(String name, String description, HackCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    protected void addProperty(PropertyMap map, AbstractProperty<?> property) {
        map.register(this, property);
    }

    protected void makeProperties(PropertyMap map) {
        addProperty(map, this.disableOnDeath);
    }

    protected boolean isGameFocused() {
        return client.player != null && client.currentScreen == null;
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;
        if (this.enabled = enabled) {
            onEnable();
        } else {
            onDisable();
        }
        winkel.getConfigManager().enabledHacks.save();
    }

    public String getName() {
        return name;
    }

    public Text getButtonText() {
        MutableText text = Text.literal(this.name);
        if (this.isEnabled()) {
            text.formatted(Formatting.GRAY);
        }
        return text;
    }

    public String getDescription() {
        return description;
    }

    public HackCategory getCategory() {
        return category;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public BooleanProperty getDisableOnDeath() {
        return disableOnDeath;
    }

}
