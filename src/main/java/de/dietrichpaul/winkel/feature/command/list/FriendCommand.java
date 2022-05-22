package de.dietrichpaul.winkel.feature.command.list;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.arguments.friend.FriendArgumentType;
import de.dietrichpaul.winkel.feature.command.arguments.friend.NewFriendArgumentType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("friend");
    }

    private static int add(CommandContext<InternalCommandSource> context) {
        String friend = NewFriendArgumentType.getNewFriend(context, "friend");
        WinkelClient.INSTANCE.getFriendManager().addFriend(friend);
        WinkelClient.INSTANCE.getChat().print("command.friend.add", new LiteralText(friend).formatted(Formatting.GRAY));
        return 1;
    }

    private static int addTag(CommandContext<InternalCommandSource> context) {
        String friend = NewFriendArgumentType.getNewFriend(context, "friend");
        String tag = StringArgumentType.getString(context, "tag");
        WinkelClient.INSTANCE.getFriendManager().addFriend(friend);
        WinkelClient.INSTANCE.getChat().print("command.friend.add_tag", new LiteralText(friend).formatted(Formatting.GRAY), new LiteralText(tag).formatted(Formatting.GRAY));
        return 1;
    }

    private static int remove(CommandContext<InternalCommandSource> context) {
        String friend = NewFriendArgumentType.getNewFriend(context, "friend");
        WinkelClient.INSTANCE.getFriendManager().removeFriend(friend);
        WinkelClient.INSTANCE.getChat().print("command.friend.remove", new LiteralText(friend).formatted(Formatting.GRAY));
        return 1;
    }

    @Override
    public void build(LiteralArgumentBuilder<InternalCommandSource> base) {
        base
                .then(literal("add").then(
                        argument("friend", NewFriendArgumentType.newFriend())
                                .executes(FriendCommand::add)
                                .then(
                                        argument("tag", StringArgumentType.string())
                                                .executes(FriendCommand::addTag)
                                )
                )).then(literal("remove").then(
                        argument("friend", FriendArgumentType.friend())
                                .executes(FriendCommand::remove)
                ));
    }
}
