package de.dietrichpaul.winkel.feature.alt.offline;

import com.mojang.authlib.exceptions.AuthenticationException;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProvider;
import de.dietrichpaul.winkel.feature.alt.CredentialField;
import net.minecraft.client.MinecraftClient;

import java.util.Map;

public class OfflineAuthenticationProvider extends AuthenticationProvider<OfflineAltSession> {

    public OfflineAuthenticationProvider() {
        super("offline", false, new CredentialField[]{new CredentialField("name", false)});
    }

    @Override
    public void login(OfflineAltSession session) throws AuthenticationException {

    }

    @Override
    public void joinServer(OfflineAltSession session, MinecraftClient client, String server, String serverId) throws AuthenticationException {

    }

    @Override
    public OfflineAltSession create(Map<String, String> textBoxContent) {
        return new OfflineAltSession(textBoxContent.get("name"), this);
    }

    @Override
    public void parseClipboard(String clipboard, Map<String, String> textBoxContent) {
        textBoxContent.put("name", clipboard);
    }

}
