package de.dietrichpaul.winkel.feature.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.SimpleSuggestionBuilder;
import de.dietrichpaul.winkel.feature.hack.Hack;

import java.util.concurrent.CompletableFuture;

public class HackSuggestor implements SuggestionProvider<InternalCommandSource> {

    private HackSuggestor() {
    }

    public static HackSuggestor hack() {
        return new HackSuggestor();
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<InternalCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        return new SimpleSuggestionBuilder(builder)
                .addAll(WinkelClient.INSTANCE.getHackList().getHackNames())
                .build();
    }

}
