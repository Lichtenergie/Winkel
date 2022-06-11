package de.dietrichpaul.winkel.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Chat {

    public static final MutableText PREFIX = Text.literal("")

            // &8&l[
            .append(Text.literal("[")
                    .formatted(Formatting.DARK_GRAY, Formatting.BOLD))

            // &6&lWinkel
            .append(Text.literal("Winkel")
                    .formatted(Formatting.GOLD, Formatting.BOLD))

            // &8&l]
            .append(Text.literal("]")
                    .formatted(Formatting.DARK_GRAY, Formatting.BOLD));

    public void component(Text component) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(PREFIX.copy().append(" ").append(component));
    }

    public void error(Text component) {
        this.component(
                Text.literal("")
                        .formatted(Formatting.RED)
                        .append(component)
        );
    }

    public void print(String key, Object... args) {
        component(Text.translatable(key, args));
    }
}
