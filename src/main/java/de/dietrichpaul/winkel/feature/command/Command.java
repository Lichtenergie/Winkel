package de.dietrichpaul.winkel.feature.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import de.dietrichpaul.winkel.WinkelClient;
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

    public LiteralArgumentBuilder<InternalCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public <T> RequiredArgumentBuilder<InternalCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public abstract void build(LiteralArgumentBuilder<InternalCommandSource> base);

    public String[] getAliases() {
        return aliases;
    }

}
