package de.dietrichpaul.winkel.feature.command;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.text.Text;

public class SimpleSuggestionBuilder {

    private SuggestionsBuilder builder;

    public SimpleSuggestionBuilder(SuggestionsBuilder builder) {
        this.builder = builder;
    }

    public boolean matches(String text) {
        return text.toLowerCase(Locale.ROOT).startsWith(builder.getRemaining().toLowerCase(Locale.ROOT));
    }

    public SimpleSuggestionBuilder add(String text) {
        if (!matches(text))
            return this;
        builder.suggest(text);
        return this;
    }

    public SimpleSuggestionBuilder addDescriptive(String text, Text description) {
        if (!matches(text))
            return this;
        builder.suggest(text, description);
        return this;
    }

    public SimpleSuggestionBuilder addAll(Iterable<String> texts) {
        for (String text : texts)
            add(text);
        return this;
    }

    public SimpleSuggestionBuilder addDescriptiveAll(Map<String, Text> descriptiveTexts) {
        for (Map.Entry<String, Text> entry : descriptiveTexts.entrySet())
            addDescriptive(entry.getKey(), entry.getValue());
        return this;
    }

    public CompletableFuture<Suggestions> build() {
        return builder.buildFuture();
    }

}
