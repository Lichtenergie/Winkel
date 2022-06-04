package de.dietrichpaul.winkel.feature.command.node;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;

public class SimpleRequiredArgumentBuilder<S, T> extends SimpleArgumentBuilder<S, SimpleRequiredArgumentBuilder<S, T>> {
    private final String name;
    private final ArgumentType<T> type;
    private SuggestionProvider<S> suggestionsProvider = null;

    private SimpleRequiredArgumentBuilder(final String name, final ArgumentType<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <S, T> SimpleRequiredArgumentBuilder<S, T> argument(final String name, final ArgumentType<T> type) {
        return new SimpleRequiredArgumentBuilder<>(name, type);
    }

    public SimpleRequiredArgumentBuilder<S, T> suggests(final SuggestionProvider<S> provider) {
        this.suggestionsProvider = provider;
        return getThis();
    }

    public SuggestionProvider<S> getSuggestionsProvider() {
        return suggestionsProvider;
    }

    @Override
    protected SimpleRequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    public ArgumentType<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(getName(), getType(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }

}
