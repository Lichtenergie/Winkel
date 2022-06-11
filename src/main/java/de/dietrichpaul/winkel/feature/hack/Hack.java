package de.dietrichpaul.winkel.feature.hack;

import de.dietrichpaul.winkel.WinkelClient;
import net.minecraft.client.MinecraftClient;
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

    private boolean enabled;

    public Hack(String name, String description, HackCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    protected void onEnable() {
    }

    protected void onDisable() {
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

}
