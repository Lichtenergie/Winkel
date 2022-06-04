package de.dietrichpaul.winkel.feature.command.list;

import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.arguments.HackArgumentType;
import de.dietrichpaul.winkel.feature.command.node.SimpleBaseArgumentBuilder;
import de.dietrichpaul.winkel.feature.hack.Hack;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "t");
    }

    @Override
    public void build(SimpleBaseArgumentBuilder<InternalCommandSource> base) {
        base.then(
                argument("hack", HackArgumentType.hack(winkel.getHackList()))
                        .executes(ctx -> {
                            Hack hack = HackArgumentType.getHack(ctx, "hack");
                            hack.toggle();
                        })
        );
    }

}
