package de.dietrichpaul.winkel.feature.command.arguments.input;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.SimpleSuggestionBuilder;
import de.dietrichpaul.winkel.util.keyboard.WKey;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;

import java.util.concurrent.CompletableFuture;

public class KeyArgumentType implements ArgumentType<WKey> {

    public static final DynamicCommandExceptionType INPUT_UNKNOWN = new DynamicCommandExceptionType(
            arg -> new TranslatableText("arguments.keyboard.unknown", arg));

    private KeyArgumentType() {
    }


    public static KeyArgumentType key() {
        return new KeyArgumentType();
    }

    public static WKey getKey(CommandContext<?> context, String name) {
        return context.getArgument(name, WKey.class);
    }

    @Override
    public WKey parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        if (!WinkelClient.INSTANCE.getKeyboardMapper().getNameKeyMap().containsKey(string))
            throw INPUT_UNKNOWN.createWithContext(reader, string);
        return new WKey(WinkelClient.INSTANCE.getKeyboardMapper().fromName(string), string);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return new SimpleSuggestionBuilder(builder)
                .addAll(WinkelClient.INSTANCE.getKeyboardMapper().getNameKeyMap().keySet())
                .build();
    }

}
