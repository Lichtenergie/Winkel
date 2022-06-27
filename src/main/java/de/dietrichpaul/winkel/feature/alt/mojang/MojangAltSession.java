package de.dietrichpaul.winkel.feature.alt.mojang;

import com.mojang.authlib.GameProfile;
import de.dietrichpaul.winkel.feature.alt.AltSession;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProvider;

import java.util.UUID;

public class MojangAltSession extends AltSession<MojangAltSession> {

    private final MojangAuthenticationProvider provider;
    String email;
    String password;

    String name;
    UUID uuid;
    GameProfile gameProfile;
    String session;

    public MojangAltSession(MojangAuthenticationProvider provider, String email, String password) {
        this.provider = provider;
        this.email = email;
        this.password = password;
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
        return this.gameProfile;
    }

    @Override
    public AuthenticationProvider<MojangAltSession> getProvider() {
        return this.provider;
    }

}
