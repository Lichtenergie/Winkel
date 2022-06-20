package de.dietrichpaul.winkel.injection.accessor.client.renderer;

import net.minecraft.client.MinecraftClient;

public interface ICameraMixin {

    void setClient(MinecraftClient client);

    void accessRotation(float yaw, float pitch);

}
