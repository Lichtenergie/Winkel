package de.dietrichpaul.winkel.feature.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.Chat;
import de.dietrichpaul.winkel.feature.command.list.*;
import de.dietrichpaul.winkel.feature.command.node.SimpleBaseArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.node.SimpleLiteralArgumentBuilder;
import net.minecraft.server.command.HelpCommand;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public class CommandManager {

    private final CommandDispatcher<InternalCommandSource> dispatcher = new CommandDispatcher<>();

    private final CommandListener commandListener;

    public CommandManager() {
        this.commandListener = new CommandListener(this);
        this.commandListener.register(WinkelClient.INSTANCE.getEventDispatcher());
        this.addCommand(new AltCommand());
        this.addCommand(new FriendCommand());
        this.addCommand(new MacroCommand());
        this.addCommand(new PropertyCommand());
        this.addCommand(new ToggleCommand());
    }

    public void execute(StringReader input, InternalCommandSource source) {
        try {
            this.dispatcher.execute(input, source);
        } catch (CommandSyntaxException e) {
            Chat chat = WinkelClient.INSTANCE.getChat();

            // Print the error message
            chat.error(Texts.toText(e.getRawMessage()));

            // verbose
            if (e.getCursor() >= 0) {
                int index = Math.min(e.getInput().length(), e.getCursor());

                MutableText verbose = new LiteralText("")
                        .formatted(Formatting.GRAY)
                        .styled(style -> style
                                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, input.getString())));

                if (index > 10) {
                    verbose.append("...");
                }

                verbose.append(e.getInput().substring(Math.max(0, index - 10), index));
                if (index < e.getInput().length()) {
                    verbose.append(new LiteralText(e.getInput().substring(index))
                            .formatted(Formatting.RED, Formatting.UNDERLINE));
                }
                verbose.append(new TranslatableText("command.context.here")
                        .formatted(Formatting.RED, Formatting.ITALIC));

                chat.error(verbose);
            }

        }
    }

    public void addCommand(Command command) {
        for (String alias : command.getAliases()) {
            SimpleBaseArgumentBuilder<InternalCommandSource> literal = SimpleBaseArgumentBuilder.literal(alias);
            command.build(literal);
            dispatcher.register(literal);
        }
    }

    public int getCommandCount() {
        return this.dispatcher.getRoot().getChildren().size();
    }

    public CommandDispatcher<InternalCommandSource> getDispatcher() {
        return dispatcher;
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }

}
