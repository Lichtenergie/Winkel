package de.dietrichpaul.winkel.feature.command.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dietrichpaul.winkel.feature.hack.Hack;
import de.dietrichpaul.winkel.feature.hack.HackList;

import java.util.concurrent.CompletableFuture;

public class HackArgumentType implements ArgumentType<Hack> {

    public static final DynamicCommandExceptionType UNKNOWN_HACK
            = new DynamicCommandExceptionType(hackName -> new LiteralMessage("Unknown hack \"" + hackName + "\""));

    private final HackList hackMap;

    private HackArgumentType(HackList hackMap) {
        this.hackMap = hackMap;
    }

    public static HackArgumentType hack(HackList hackMap) {
        return new HackArgumentType(hackMap);
    }

    public static Hack getHack(CommandContext<?> context, String name) {
        return context.getArgument(name, Hack.class);
    }

    @Override
    public Hack parse(StringReader reader) throws CommandSyntaxException {
        String hackName = reader.readString();
        Hack hack = this.hackMap.getHack(hackName);
        if (hack == null) {
            throw UNKNOWN_HACK.createWithContext(reader, hackName);
        }
        return hack;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String s : this.hackMap.getHackNames()) {
            if (s.toLowerCase().startsWith(builder.getRemainingLowerCase())) builder.suggest(s);
        }
        return builder.buildFuture();
    }

}
