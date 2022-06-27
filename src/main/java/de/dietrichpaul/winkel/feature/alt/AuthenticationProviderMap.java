package de.dietrichpaul.winkel.feature.alt;

import de.dietrichpaul.winkel.feature.alt.easymc.EasyMCAuthenticationProvider;
import de.dietrichpaul.winkel.feature.alt.mojang.MojangAuthenticationProvider;
import de.dietrichpaul.winkel.feature.alt.offline.OfflineAuthenticationProvider;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class AuthenticationProviderMap {

    private Map<String, AuthenticationProvider<?>> providers = new TreeMap<>(String::compareTo);

    public AuthenticationProviderMap() {
        addProvider(new EasyMCAuthenticationProvider());
        addProvider(new MojangAuthenticationProvider());
        addProvider(new OfflineAuthenticationProvider());
    }

    public void addProvider(AuthenticationProvider<?> provider) {
        this.providers.put(provider.getName(), provider);
    }

    public Map<String, AuthenticationProvider<?>> getProviders() {
        return providers;
    }

}
