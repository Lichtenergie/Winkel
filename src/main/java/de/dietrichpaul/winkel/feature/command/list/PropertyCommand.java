package de.dietrichpaul.winkel.feature.command.list;

import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.node.SimpleBaseArgumentBuilder;
import de.dietrichpaul.winkel.property.AbstractProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;
import java.util.Set;

public class PropertyCommand extends Command {

    public PropertyCommand() {
        super("property");
    }

    @Override
    public void build(SimpleBaseArgumentBuilder<InternalCommandSource> base) {
        for (Map.Entry<StringIdentifiable, Set<AbstractProperty<?>>> assignment : winkel.getPropertyMap().getProperties().entrySet()) {
            for (AbstractProperty<?> property : assignment.getValue()) {
                SimpleArgumentBuilder<InternalCommandSource, ?> argumentBuilder = literal(property.getLowerCamelCase())
                        .executes(context -> {
                            Text propertyName = Text.literal(property.getName())
                                    .formatted(Formatting.GRAY);
                            Text parentName = Text.literal(assignment.getKey().asString())
                                    .formatted(Formatting.GRAY);
                            winkel.getChat().print("command.property.state", propertyName, parentName, Text.literal("").append(property.getValueText()).formatted(Formatting.GRAY));
                            return 1;
                        });
                property.makeCommand(argumentBuilder);
                if (argumentBuilder == null)
                    continue;
                base.then(literal(assignment.getKey().asString())
                                .then(argumentBuilder));
            }
        }
    }

}
