package de.dietrichpaul.winkel.feature.alt.mojang;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProvider;
import de.dietrichpaul.winkel.feature.alt.CredentialField;
import net.minecraft.client.MinecraftClient;
import org.checkerframework.checker.units.qual.C;

import java.net.Proxy;
import java.util.Map;

public class MojangAuthenticationProvider extends AuthenticationProvider<MojangAltSession> {

    private static YggdrasilAuthenticationService yggdrasil = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");

    public MojangAuthenticationProvider() {
        super("mojang", false, new CredentialField[]{
                new CredentialField("email", false),
                new CredentialField("password", true)
        });
    }

    @Override
    public void login(MojangAltSession session) throws AuthenticationException {
        UserAuthentication userAuthentication = yggdrasil.createUserAuthentication(Agent.MINECRAFT);
        userAuthentication.setUsername(session.email);
        userAuthentication.setPassword(session.password);
        userAuthentication.logIn();

        session.session = userAuthentication.getAuthenticatedToken();
        session.name = userAuthentication.getSelectedProfile().getName();
        session.uuid = userAuthentication.getSelectedProfile().getId();
        session.gameProfile = new GameProfile(session.uuid, session.name);
    }

    @Override
    public void joinServer(MojangAltSession session, MinecraftClient client, String server, String serverId) throws AuthenticationException {
        client.getSessionService().joinServer(session.gameProfile, session.session, serverId);
    }

    @Override
    public MojangAltSession create(Map<String, String> textBoxContent) {
        return new MojangAltSession(this, textBoxContent.get("email"), textBoxContent.get("password"));
    }

    @Override
    public void parseClipboard(String clipboard, Map<String, String> textBoxContent) {
        String[] split = clipboard.split(":");
        textBoxContent.put("email", split[0]);
        textBoxContent.put("password", split[1]);
    }

}
