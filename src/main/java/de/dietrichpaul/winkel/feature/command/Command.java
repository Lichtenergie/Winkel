package de.dietrichpaul.winkel.feature.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.node.SimpleBaseArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.node.SimpleLiteralArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.node.SimpleRequiredArgumentBuilder;
import net.minecraft.client.MinecraftClient;

public abstract class Command {
    private final String[] aliases;

    protected WinkelClient winkel = WinkelClient.INSTANCE;
    protected MinecraftClient client = MinecraftClient.getInstance();

    public Command(String name, String... aliases) {
        this.aliases = new String[aliases.length + 1];
        this.aliases[0] = name;
        System.arraycopy(aliases, 0, this.aliases, 1, aliases.length);
    }

    public static SimpleLiteralArgumentBuilder<InternalCommandSource> literal(String name) {
        return SimpleLiteralArgumentBuilder.literal(name);
    }

    public static <T> SimpleRequiredArgumentBuilder<InternalCommandSource, T> argument(String name, ArgumentType<T> type) {
        return SimpleRequiredArgumentBuilder.argument(name, type);
    }

    public abstract void build(SimpleBaseArgumentBuilder<InternalCommandSource> base);

    public String[] getAliases() {
        return aliases;
    }
}
