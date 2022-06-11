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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class NewFriendArgumentType implements ArgumentType<String> {

    private final static DynamicCommandExceptionType FRIEND_ALREADY_EXISTING = new DynamicCommandExceptionType(o -> Text.translatable("arguments.new-friend.already_existing", o));

    private NewFriendArgumentType() {
    }

    public static NewFriendArgumentType newFriend() {
        return new NewFriendArgumentType();
    }

    public static String getNewFriend(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        SimpleSuggestionBuilder suggestionBuilder = new SimpleSuggestionBuilder(builder);
        for (PlayerListEntry playerListEntry : MinecraftClient.getInstance().getNetworkHandler().getPlayerList()) {
            if (playerListEntry.getProfile().getName().equals(MinecraftClient.getInstance().player.getGameProfile().getName()))
                continue;
            suggestionBuilder.add(playerListEntry.getProfile().getName());
        }
        return suggestionBuilder.build();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();
        if (WinkelClient.INSTANCE.getFriendManager().isFriend(name))
            throw FRIEND_ALREADY_EXISTING.createWithContext(reader, name);
        return name;
    }

}