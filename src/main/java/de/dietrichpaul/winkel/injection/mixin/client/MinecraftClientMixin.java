package de.dietrichpaul.winkel.injection.mixin.client;

import ca.weblite.objc.Client;
import de.dietrichpaul.winkel.WinkelClient;
import de.dietrichpaul.winkel.event.list.GameTickListener;
import de.dietrichpaul.winkel.injection.accessor.client.IMinecraftClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.resource.ClientBuiltinResourcePackProvider;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ResourceManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Mixin(value = MinecraftClient.class, priority = Integer.MIN_VALUE)
public abstract class MinecraftClientMixin implements IMinecraftClientMixin {

    @Shadow
    @Final
    public static boolean IS_SYSTEM_MAC;
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    private int itemUseCooldown;
    @Shadow
    @Final
    private Window window;

    @Shadow
    public abstract ClientBuiltinResourcePackProvider getResourcePackProvider();

    @Shadow
    public abstract ResourceManager getResourceManager();

    @Mutable
    @Shadow @Final private Session session;

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        WinkelClient.INSTANCE.getEventDispatcher().post(GameTickListener.GameTickEvent.INSTANCE);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setFramerateLimit(I)V", shift = At.Shift.BEFORE))
    public void onInit(RunArgs args, CallbackInfo ci) {
        try {
            final String folder = "/assets/winkel/icons/run/";
            final InputStream icon16x16 = this.getClass().getResourceAsStream(folder + "16x16.png");
            final InputStream icon32x32 = this.getClass().getResourceAsStream(folder + "32x32.png");
            final InputStream iconMac = this.getClass().getResourceAsStream(folder + "mac.icns");
            if (!IS_SYSTEM_MAC) this.window.setIcon(icon16x16, icon32x32);
            else {
                String string = Base64.getEncoder().encodeToString(iconMac.readAllBytes());
                Client client = Client.getInstance();
                Object object = client.sendProxy("NSData", "alloc",
                        new Object[0]).send("initWithBase64Encoding:", string);
                Object object2 = client.sendProxy("NSImage", "alloc",
                        new Object[0]).send("initWithData:", object);
                client.sendProxy("NSApplication", "sharedApplication",
                        new Object[0]).send("setApplicationIconImage:", object2);
            }
        } catch (final IOException exception) {
            LOGGER.error("couldn't set icon", exception);
        }
        WinkelClient.INSTANCE.start();
    }

    @Override
    public int getItemUseCooldown() {
        return this.itemUseCooldown;
    }

    @Override
    public void setItemUseCooldown(int ticks) {
        this.itemUseCooldown = ticks;
    }

    @Override
    public Session getSession() {
        return this.session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

}
