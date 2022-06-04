package de.dietrichpaul.winkel.feature.alt;

import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.MinecraftClient;

import java.util.Map;

public abstract class AuthenticationProvider<T extends AltSession> {

    private String name;
    private boolean directLoginOnly;
    private CredentialField[] credentialField;

    public AuthenticationProvider(String name, boolean directLoginOnly, CredentialField[] credentialField) {
        this.name = name;
        this.directLoginOnly = directLoginOnly;
        this.credentialField = credentialField;
    }

    public abstract void login(T session) throws AuthenticationException;

    public abstract void joinServer(T session, MinecraftClient client, String server, String serverId) throws AuthenticationException;

    public abstract T create(Map<String, String> textBoxContent);

    public abstract void parseClipboard(String clipboard, Map<String, String> textBoxContent);

    public String getName() {
        return name;
    }

    public boolean isDirectLoginOnly() {
        return directLoginOnly;
    }

    public CredentialField[] getCredentialField() {
        return credentialField;
    }

}
