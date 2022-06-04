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
import net.minecraft.text.LiteralText;

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
            SimpleLiteralArgumentBuilder<InternalCommandSource> start = literal(entry.getKey().toLowerCase(Locale.ROOT));
            SimpleArgumentBuilder<InternalCommandSource, ?> iterativeTemp = start;
            AuthenticationProvider authenticationProvider = entry.getValue();
            CredentialField[] field = authenticationProvider.getCredentialField();
            for (int i = 0; i < field.length; i++) {
                CredentialField credentialField = field[i];
                SimpleArgumentBuilder<InternalCommandSource, ?> temp = argument(credentialField.getName(), StringArgumentType.string());
                if (i == field.length - 1) {
                    temp.executes(ctx -> {
                        Map<String, String> textBoxes = new HashMap<>();
                        for (CredentialField tempField : authenticationProvider.getCredentialField()) {
                            textBoxes.put(tempField.getName(), StringArgumentType.getString(ctx, tempField.getName()));
                        }
                        AltSession session = authenticationProvider.create(textBoxes);
                        try {
                            authenticationProvider.login(session);
                            winkel.setAltSession(session);
                        } catch (AuthenticationException e) {
                            winkel.getChat().error(new LiteralText(e.getMessage()));
                        }
                    });
                }
                iterativeTemp.then(temp);
                iterativeTemp = temp;
            }
            base.then(start);
        }
    }

}
