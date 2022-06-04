package de.dietrichpaul.winkel.feature.alt.easymc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.util.UUIDTypeAdapter;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProvider;
import de.dietrichpaul.winkel.feature.alt.CredentialField;
import de.dietrichpaul.winkel.util.WebUtil;
import net.minecraft.client.MinecraftClient;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;
import java.util.Map;

public class EasyMCAuthenticationProvider extends AuthenticationProvider<EasyMCAltSession> {

    private static String redeemURL = "https://api.easymc.io/v1/token/redeem";
    private static String joinServerURL = "https://sessionserver.easymc.io/session/minecraft/join";

    public EasyMCAuthenticationProvider() {
        super("EasyMC", true, new CredentialField[]{new CredentialField("token", true)});
    }

    @Override
    public void login(EasyMCAltSession session) throws AuthenticationException {
        JsonObject authRequestBody = new JsonObject();
        authRequestBody.addProperty("token", session.token);

        JsonObject redeem;
        try {
            redeem = JsonParser.parseString(WebUtil.postJson(redeemURL, authRequestBody)).getAsJsonObject();
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 403)
                throw new InvalidCredentialsException("Invalid token");
            throw new AuthenticationException("Cannot authenticate", e);
        } catch (IOException e) {
            throw new AuthenticationUnavailableException("No connection", e);
        }

        session.name = redeem.get("mcName").getAsString();
        session.session = redeem.get("session").getAsString();
        session.uuid = UUIDTypeAdapter.fromString(redeem.get("uuid").getAsString());
        session.gameProfile = new GameProfile(session.uuid, session.name);
    }

    @Override
    public void joinServer(EasyMCAltSession session, MinecraftClient client, String server, String serverId) throws AuthenticationException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("accessToken", session.session);
        requestBody.addProperty("selectedProfile", UUIDTypeAdapter.fromUUID(session.uuid));
        requestBody.addProperty("serverId", serverId);

        try {
            String s = WebUtil.postJson(joinServerURL, requestBody);
            if (!s.isEmpty())
                throw new InvalidCredentialsException();
        } catch (HttpResponseException e) {
            throw new InvalidCredentialsException();
        } catch (IOException e) {
            throw new AuthenticationUnavailableException();
        }
    }

    @Override
    public EasyMCAltSession create(Map<String, String> textBoxContent) {
        return new EasyMCAltSession(this, textBoxContent.get("token"));
    }

    @Override
    public void parseClipboard(String clipboard, Map<String, String> textBoxContent) {

    }

}
