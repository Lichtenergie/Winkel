package de.dietrichpaul.winkel.feature.alt.offline;

import com.mojang.authlib.GameProfile;
import de.dietrichpaul.winkel.feature.alt.AltSession;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProvider;

import java.util.UUID;

public class OfflineAltSession extends AltSession<OfflineAltSession> {

    private String name;
    private OfflineAuthenticationProvider authenticationProvider;

    public OfflineAltSession(String name, OfflineAuthenticationProvider authenticationProvider) {
        this.name = name;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public GameProfile getGameProfile() {
        return new GameProfile(null, this.name);
    }

    @Override
    public AuthenticationProvider<OfflineAltSession> getProvider() {
        return this.authenticationProvider;
    }

}
