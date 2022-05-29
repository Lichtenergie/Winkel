package de.dietrichpaul.winkel.feature.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dietrichpaul.winkel.feature.command.SimpleSuggestionBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EnumArgument<T extends Enum<T>> implements ArgumentType<T> {

    private static final SimpleCommandExceptionType ENUM_CONSTANT_NOT_EXISTING =
            new SimpleCommandExceptionType(Text.of("the entered enum constant doesn't exist"));
    private static final Collection<String> EXAMPLES = Arrays.asList("A", "B");

    private final T[] values;

    private EnumArgument(T[] values) {
        this.values = values;
    }

    public static <T extends Enum<T>> EnumArgument<T> enumArgument(T[] values) {
        return new EnumArgument<T>(values);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getEnumConstant(CommandContext<?> commandContext, String name) {
        return (T) commandContext.getArgument(name, Enum.class);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        T type = null;
        String name = reader.readString();
        for (T t : values) {
            String n = t.toString();
            if (t instanceof StringIdentifiable identifiable)
                n = identifiable.asString();
            if (n.equals(name)) {
                type = t;
                break;
            }
        }
        if (type == null)
            throw ENUM_CONSTANT_NOT_EXISTING.createWithContext(reader);
        return type;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return new SimpleSuggestionBuilder(builder)
                .addAll(Arrays.stream(this.values).map(t -> {
                    if (t instanceof StringIdentifiable identifiable)
                        return identifiable.asString();
                    return t.toString();
                }).collect(Collectors.toList()))
                .build();
    }

}
