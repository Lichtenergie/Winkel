package de.dietrichpaul.winkel.injection.accessor.client;

import net.minecraft.client.util.Session;

public interface IMinecraftClientMixin {

    int getItemUseCooldown();

    void setItemUseCooldown(int ticks);

    Session getSession();

    void setSession(Session session);

    int getFPS();

}
