package de.dietrichpaul.winkel.feature.command.arguments.target;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dietrichpaul.winkel.feature.command.SimpleSuggestionBuilder;
import de.dietrichpaul.winkel.property.list.target.TargetFilter;
import de.dietrichpaul.winkel.property.list.target.TargetSelectionProperty;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TargetSelectionRemoveArgumentType implements ArgumentType<TargetFilter> {

    private static final DynamicCommandExceptionType TARGET_FILTER_NOT_FOUND = new DynamicCommandExceptionType(o -> Text.literal("Filter not found \"" + o + "\""));
    private static final DynamicCommandExceptionType TARGET_FILTER_NOT_PRESENT = new DynamicCommandExceptionType(o -> Text.literal("Filter isn't present \"" + o + "\""));

    private TargetSelectionProperty property;

    private TargetSelectionRemoveArgumentType(TargetSelectionProperty property) {
        this.property = property;
    }

    public static TargetSelectionRemoveArgumentType removeTargetSelection(TargetSelectionProperty property) {
        return new TargetSelectionRemoveArgumentType(property);
    }

    public static TargetFilter getTargetFilter(CommandContext<?> context, String name) {
        return context.getArgument(name, TargetFilter.class);
    }

    @Override
    public TargetFilter parse(StringReader reader) throws CommandSyntaxException {
        String s = reader.readString();
        TargetFilter targetFilter = TargetSelectionProperty.targetFilters.get(s);
        if (targetFilter == null)
            throw TARGET_FILTER_NOT_FOUND.createWithContext(reader, s);
        if (!this.property.getValue().contains(targetFilter))
            throw TARGET_FILTER_NOT_PRESENT.createWithContext(reader, s);
        return targetFilter;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return new SimpleSuggestionBuilder(builder)
                .addDescriptiveAll(TargetSelectionProperty.targetFilters.values().stream().filter(filter -> property.getValue().contains(filter))
                        .collect(Collectors.toMap(TargetFilter::lowerCamelCase, f -> Text.of(f.description()))))
                .build();
    }

}
