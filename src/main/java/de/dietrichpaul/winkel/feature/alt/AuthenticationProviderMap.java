package de.dietrichpaul.winkel.feature.alt;

import de.dietrichpaul.winkel.feature.alt.easymc.EasyMCAuthenticationProvider;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class AuthenticationProviderMap {

    private Map<String, AuthenticationProvider<?>> providers = new TreeMap<>(String::compareTo);

    public AuthenticationProviderMap() {
        addProvider(new EasyMCAuthenticationProvider());
    }

    public void addProvider(AuthenticationProvider<?> provider) {
        this.providers.put(provider.getName(), provider);
    }

    public Map<String, AuthenticationProvider<?>> getProviders() {
        return providers;
    }

}
