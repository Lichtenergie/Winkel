package de.dietrichpaul.winkel.feature.alt;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public abstract class AltSession<S extends AltSession<?>> {

    public abstract String getName();

    public abstract UUID getUUID();

    public abstract GameProfile getGameProfile();

    public abstract AuthenticationProvider<S> getProvider();

}
