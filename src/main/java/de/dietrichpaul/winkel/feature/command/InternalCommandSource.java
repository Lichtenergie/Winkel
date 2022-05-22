package de.dietrichpaul.winkel.feature.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class InternalCommandSource extends ClientCommandSource {

    public InternalCommandSource(ClientPlayNetworkHandler networkHandler) {
        super(networkHandler, MinecraftClient.getInstance());
    }

}
