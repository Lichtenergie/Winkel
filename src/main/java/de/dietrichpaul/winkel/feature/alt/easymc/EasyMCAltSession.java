package de.dietrichpaul.winkel.feature.alt.easymc;

import com.mojang.authlib.GameProfile;
import de.dietrichpaul.winkel.feature.alt.AltSession;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProvider;

import java.util.UUID;

public class EasyMCAltSession extends AltSession<EasyMCAltSession> {

    private AuthenticationProvider<EasyMCAltSession> provider;

    String token;
    String session;
    UUID uuid;
    String name;
    GameProfile gameProfile;

    public EasyMCAltSession(AuthenticationProvider<EasyMCAltSession> provider, String token) {
        this.provider = provider;
        this.token = token;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public GameProfile getGameProfile() {
        return gameProfile;
    }

    @Override
    public AuthenticationProvider<EasyMCAltSession> getProvider() {
        return this.provider;
    }

}
