package de.dietrichpaul.winkel.feature.command.list;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.arguments.input.BoundKeyArgumentType;
import de.dietrichpaul.winkel.feature.command.arguments.input.KeyArgumentType;
import de.dietrichpaul.winkel.feature.command.node.SimpleBaseArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.suggestion.HackSuggestor;
import de.dietrichpaul.winkel.util.keyboard.WKey;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.List;

public class MacroCommand extends Command {

    public MacroCommand() {
        super("macro");
    }

    @Override
    public void build(SimpleBaseArgumentBuilder<InternalCommandSource> base) {
            base.then(
                    literal("add").then(
                            argument("key", KeyArgumentType.key()).then(
                                    argument("action", StringArgumentType.greedyString())
                                            .suggests(HackSuggestor.hack())
                                            .executes(this::add)
                            )
                    )
            ).then(
                    literal("list").then(
                            argument("key", BoundKeyArgumentType.boundKey())
                                    .executes(this::list)
                    )
            ).then(
                    literal("remove").then(
                            argument("key", BoundKeyArgumentType.boundKey())
                                    .executes(this::remove)
                    )
            );
    }

    private void list(CommandContext<InternalCommandSource> context) {
        WKey key = BoundKeyArgumentType.getKey(context, "key");

        List<String> actions = WinkelClient.INSTANCE.getMacroList().getActions(key.getName());

        WinkelClient.INSTANCE.getChat().print("command.macro.list.header",
                new LiteralText(key.getName()).formatted(Formatting.GRAY));
        for (String action : actions) {
            WinkelClient.INSTANCE.getChat().print("command.macro.list.entry", new LiteralText(action)
                    .formatted(Formatting.GRAY));
        }

    }

    private void remove(CommandContext<InternalCommandSource> context) {
        WKey key = KeyArgumentType.getKey(context, "key");

        WinkelClient.INSTANCE.getMacroList().remove(key.getName());

        WinkelClient.INSTANCE.getChat().print("command.macro.remove",
                new LiteralText(key.getName()).formatted(Formatting.GRAY));

    }

    private void add(CommandContext<InternalCommandSource> context) {
        WKey key = KeyArgumentType.getKey(context, "key");
        String action = StringArgumentType.getString(context, "action");

        WinkelClient.INSTANCE.getMacroList().bind(key.getName(), action);
        WinkelClient.INSTANCE.getChat().print("command.macro.add",
                new LiteralText(action).formatted(Formatting.GRAY),
                new LiteralText(key.getName()).formatted(Formatting.GRAY));

    }

}
