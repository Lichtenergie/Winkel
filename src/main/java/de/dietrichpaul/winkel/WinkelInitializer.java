package de.dietrichpaul.winkel;

import net.fabricmc.api.ClientModInitializer;

public class WinkelInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WinkelClient.INSTANCE.init();
    }

}
