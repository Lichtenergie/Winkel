package de.dietrichpaul.winkel.feature.alt;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.util.Optional;
import java.util.function.Supplier;

public class SessionAdapter extends Session {

    private Session prev;
    private final Supplier<AltSession> altServiceSupplier;

    public SessionAdapter(Session prev, Supplier<AltSession> altServiceSupplier) {
        super(null, null, null, Optional.empty(), Optional.empty(), AccountType.MOJANG);
        this.altServiceSupplier = altServiceSupplier;
        this.prev = prev;
    }

    @Override
    public GameProfile getProfile() {
        AltSession altService = altServiceSupplier.get();
        if (altService != null && altService.getGameProfile() != null) return altService.getGameProfile();
        return this.prev.getProfile();
    }

    @Override
    public String getAccessToken() {
        AltSession altService = altServiceSupplier.get();
        if (altService != null && altService.getGameProfile() != null) return null;
        return this.prev.getAccessToken();
    }

    @Override
    public String getUsername() {
        AltSession altService = altServiceSupplier.get();
        if (altService != null && altService.getGameProfile() != null) return altService.getName();
        return this.prev.getUsername();
    }

    @Override
    public String getUuid() {
        AltSession altService = altServiceSupplier.get();
        if (altService != null && altService.getGameProfile() != null) return altService.getUUID().toString();
        return this.prev.getUuid();
    }

    @Override
    public String getSessionId() {
        AltSession altService = altServiceSupplier.get();
        if (altService != null && altService.getGameProfile() != null)  return null;
        return this.prev.getSessionId();
    }

    public void joinServer(MinecraftClient client, String server, String serverId) throws AuthenticationException {
        AltSession altService = altServiceSupplier.get();
        if (altService != null && altService.getGameProfile() != null) {
            altService.getProvider().joinServer(altService, client, server, serverId);
            return;
        }
        client.getSessionService().joinServer(getProfile(), getAccessToken(), serverId);
    }

}
