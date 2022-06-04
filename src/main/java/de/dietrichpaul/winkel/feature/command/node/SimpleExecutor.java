package de.dietrichpaul.winkel.feature.command.node;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import java.util.function.Consumer;

public interface SimpleExecutor<S, T extends ArgumentBuilder<S, T>> {

    T executes(Command<S> command);

    default T executes(Runnable command)  {
        return executes(ctx -> {
            command.run();
            return 1;
        });
    }

    default T executes(Consumer<CommandContext<S>> command) {
        return executes(ctx -> {
            command.accept(ctx);
            return 1;
        });
    }

}
