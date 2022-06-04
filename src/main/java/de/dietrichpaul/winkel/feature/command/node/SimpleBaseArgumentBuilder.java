package de.dietrichpaul.winkel.feature.command.node;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class SimpleBaseArgumentBuilder<S> extends LiteralArgumentBuilder<S> implements SimpleExecutor<S, LiteralArgumentBuilder<S>> {

    protected SimpleBaseArgumentBuilder(String literal) {
        super(literal);
    }

    public static <S> SimpleBaseArgumentBuilder<S> literal(final String name) {
        return new SimpleBaseArgumentBuilder<>(name);
    }

}
