package de.dietrichpaul.winkel.injection.accessor.client;

public interface IMinecraftClientMixin {

    int getItemUseCooldown();

    void setItemUseCooldown(int ticks);

}
