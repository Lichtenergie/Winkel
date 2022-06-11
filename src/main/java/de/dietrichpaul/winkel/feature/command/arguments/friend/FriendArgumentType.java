package de.dietrichpaul.winkel.feature.command.arguments.friend;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.SimpleSuggestionBuilder;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class FriendArgumentType implements ArgumentType<String> {

    private static final DynamicCommandExceptionType FRIEND_NOT_FOUND = new DynamicCommandExceptionType(o -> Text.translatable("arguments.friend.not_found", o));

    private FriendArgumentType() {
    }

    public static FriendArgumentType friend() {
        return new FriendArgumentType();
    }

    public static String getFriend(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();
        if (!WinkelClient.INSTANCE.getFriendManager().isFriend(name))
            throw FRIEND_NOT_FOUND.createWithContext(reader, name);
        return name;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return new SimpleSuggestionBuilder(builder)
                .addAll(WinkelClient.INSTANCE.getFriendManager().getFriendTags().keySet())
                .build();
    }
}