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
import net.minecraft.text.TranslatableText;
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
     * @author Mojang Studios, AdvancedCode
     */
    @Overwrite
    private @Nullable Text joinServerSession(String serverId) {
        try {
            if (WinkelClient.INSTANCE.getAltSession() != null) {
                WinkelClient.INSTANCE.getAltSession().getProvider().joinServer(WinkelClient.INSTANCE.getAltSession(), client, client.getCurrentServerEntry().address, serverId);
            } else {
                this.getSessionService().joinServer(this.client.getSession().getProfile(), this.client.getSession().getAccessToken(), serverId);
            }
        } catch (AuthenticationUnavailableException authenticationUnavailableException) {
            return new TranslatableText("disconnect.loginFailedInfo", new TranslatableText("disconnect.loginFailedInfo.serversUnavailable"));
        } catch (InvalidCredentialsException authenticationUnavailableException) {
            return new TranslatableText("disconnect.loginFailedInfo", new TranslatableText("disconnect.loginFailedInfo.invalidSession"));
        } catch (InsufficientPrivilegesException authenticationUnavailableException) {
            return new TranslatableText("disconnect.loginFailedInfo", new TranslatableText("disconnect.loginFailedInfo.insufficientPrivileges"));
        } catch (AuthenticationException authenticationUnavailableException) {
            return new TranslatableText("disconnect.loginFailedInfo", authenticationUnavailableException.getMessage());
        }
        return null;
    }

}
