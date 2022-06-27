package de.dietrichpaul.winkel.feature.command.list;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.dietrichpaul.winkel.feature.alt.AltSession;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProvider;
import de.dietrichpaul.winkel.feature.alt.CredentialField;
import de.dietrichpaul.winkel.feature.command.Command;
import de.dietrichpaul.winkel.feature.command.InternalCommandSource;
import de.dietrichpaul.winkel.feature.command.node.SimpleArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.node.SimpleBaseArgumentBuilder;
import de.dietrichpaul.winkel.feature.command.node.SimpleLiteralArgumentBuilder;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AltCommand extends Command {

    public AltCommand() {
        super("alt");
    }

    @Override
    public void build(SimpleBaseArgumentBuilder<InternalCommandSource> base) {
        for (Map.Entry<String, AuthenticationProvider<?>> entry : winkel.getAuthenticationProviderMap().getProviders().entrySet()) {
            SimpleArgumentBuilder<InternalCommandSource, ?> temp = null;
            for (int i = entry.getValue().getCredentialField().length - 1; i >= 0; i--) {
                if (i == entry.getValue().getCredentialField().length - 1) {
                    temp = argument(entry.getValue().getCredentialField()[i].getName(), StringArgumentType.string())
                            .executes(ctx -> {
                                Map<String, String> textBoxes = new HashMap<>();
                                for (CredentialField tempField : entry.getValue().getCredentialField()) {
                                    textBoxes.put(tempField.getName(), StringArgumentType.getString(ctx, tempField.getName()));
                                }
                                AltSession session = entry.getValue().create(textBoxes);
                                try {
                                    ((AuthenticationProvider) entry.getValue()).login(session);
                                    winkel.setAltSession(session);
                                } catch (AuthenticationException e) {
                                    winkel.getChat().error(Text.literal(e.getMessage()));
                                }
                            });
                    continue;
                }
                temp = argument(entry.getValue().getCredentialField()[i].getName(), StringArgumentType.string())
                        .then(temp);
            }
            base.then(literal(entry.getKey()).then(temp));
        }
    }

}
