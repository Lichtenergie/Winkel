package de.dietrichpaul.winkel.injection.mixin.client.network;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import de.dietrichpaul.winkel.WinkelClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientLoginNetworkHandler.class)
public abstract class ClientLoginNetworkHandlerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private ClientConnection connection;

    @Shadow
    protected abstract MinecraftSessionService getSessionService();

    /**
     * @author Mojang Studios, AdvancedCode, EnZaXD
     * @reason Hook Alt System
     */
    @Overwrite
    private @Nullable Text joinServerSession(String serverId) {
        try {
            if (WinkelClient.INSTANCE.getAltSession() != null) {
                WinkelClient.INSTANCE.getAltSession().getProvider().joinServer(WinkelClient.INSTANCE.getAltSession(), client, client.getCurrentServerEntry().address, serverId);
            } else {
                this.getSessionService().joinServer(this.client.getSession().getProfile(), this.client.getSession().getAccessToken(), serverId);
            }
        } catch (AuthenticationUnavailableException var3) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{Text.translatable("disconnect.loginFailedInfo.serversUnavailable")});
        } catch (InvalidCredentialsException var4) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{Text.translatable("disconnect.loginFailedInfo.invalidSession")});
        } catch (InsufficientPrivilegesException var5) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{Text.translatable("disconnect.loginFailedInfo.insufficientPrivileges")});
        } catch (AuthenticationException var6) {
            return Text.translatable("disconnect.loginFailedInfo", new Object[]{var6.getMessage()});
        }
        return null;
    }

}
