package de.dietrichpaul.winkel.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class Chat {

    public static final MutableText PREFIX = new LiteralText("")

            // &8&l[
            .append(new LiteralText("[")
                    .formatted(Formatting.DARK_GRAY, Formatting.BOLD))

            // &6&lWinkel
            .append(new LiteralText("Winkel")
                    .formatted(Formatting.GOLD, Formatting.BOLD))

            // &8&l]
            .append(new LiteralText("]")
                    .formatted(Formatting.DARK_GRAY, Formatting.BOLD));

    public void component(Text component) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(PREFIX.shallowCopy().append(" ").append(component));
    }

    public void error(Text component) {
        this.component(
                new LiteralText("")
                        .formatted(Formatting.RED)
                        .append(component)
        );
    }

    public void print(String key, Object... args) {
        component(new TranslatableText(key, args));
    }

}
