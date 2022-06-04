package de.dietrichpaul.winkel.feature.command.node;

import com.mojang.brigadier.builder.ArgumentBuilder;

public abstract class SimpleArgumentBuilder<S, T extends ArgumentBuilder<S, T>> extends ArgumentBuilder<S, T>
        implements SimpleExecutor<S, T> {

}
