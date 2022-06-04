package de.dietrichpaul.winkel.feature.command.node;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.function.Consumer;

public class SimpleLiteralArgumentBuilder <S> extends SimpleArgumentBuilder<S, SimpleLiteralArgumentBuilder<S>> {
    private final String literal;

    protected SimpleLiteralArgumentBuilder(final String literal) {
        this.literal = literal;
    }

    public static <S> SimpleLiteralArgumentBuilder<S> literal(final String name) {
        return new SimpleLiteralArgumentBuilder<>(name);
    }

    @Override
    protected SimpleLiteralArgumentBuilder<S> getThis() {
        return this;
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public LiteralCommandNode<S> build() {
        final LiteralCommandNode<S> result = new LiteralCommandNode<>(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }

}
