package de.dietrichpaul.winkel;

import com.mojang.authlib.exceptions.AuthenticationException;
import de.dietrichpaul.winkel.event.EventDispatcher;
import de.dietrichpaul.winkel.feature.Chat;
import de.dietrichpaul.winkel.feature.FriendManager;
import de.dietrichpaul.winkel.feature.MacroList;
import de.dietrichpaul.winkel.feature.Zoom;
import de.dietrichpaul.winkel.feature.alt.AltSession;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProviderMap;
import de.dietrichpaul.winkel.feature.alt.SessionAdapter;
import de.dietrichpaul.winkel.feature.alt.easymc.EasyMCAuthenticationProvider;
import de.dietrichpaul.winkel.feature.command.CommandManager;
import de.dietrichpaul.winkel.feature.hack.HackList;
import de.dietrichpaul.winkel.injection.accessor.client.IMinecraftClientMixin;
import de.dietrichpaul.winkel.util.keyboard.KeyboardMapper;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.util.Collections;

public class WinkelClient {

    public static final WinkelClient INSTANCE = new WinkelClient();

    public static final String NAME = "Winkel";
    public static final String VERSION = "2.0.0-release.1";

    private EventDispatcher eventDispatcher;
    private HackList hackList;
    private KeyboardMapper keyboardMapper;
    private CommandManager commandManager;
    private FriendManager friendManager;
    private MacroList macroList;
    private Chat chat;
    private Zoom zoom;
    private AuthenticationProviderMap authenticationProviderMap;

    private File directory;

    private AltSession altSession;

    public void init() {
        this.directory = new File(MinecraftClient.getInstance().runDirectory, "Winkel");
        this.chat = new Chat();
        this.authenticationProviderMap = new AuthenticationProviderMap();
        this.eventDispatcher = new EventDispatcher();
        this.hackList = new HackList();
        this.commandManager = new CommandManager();
        this.keyboardMapper = new KeyboardMapper();
        this.friendManager = new FriendManager();
        this.macroList = new MacroList();
        this.zoom = new Zoom();

        System.out.println("ALARM");
        this.altSession = new EasyMCAuthenticationProvider().create(Collections.singletonMap("token", "dijgr25EwHU7diVtZFXa"));
        try {
            System.out.println("LOGGING IN AAPIOJDPOIJASOPDIJPSIOJDAPIOJAPWDOJ");
            this.altSession.getProvider().login(this.altSession);
            System.out.println("LOGGED IN P)JDPOIJHA=D)JQ");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.keyboardMapper.start();

        IMinecraftClientMixin imc = (IMinecraftClientMixin) MinecraftClient.getInstance();
        imc.setSession(new SessionAdapter(imc.getSession(), this::getAltSession));
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public KeyboardMapper getKeyboardMapper() {
        return keyboardMapper;
    }

    public HackList getHackList() {
        return hackList;
    }

    public MacroList getMacroList() {
        return macroList;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Chat getChat() {
        return chat;
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    public AltSession getAltSession() {
        return altSession;
    }

    public void setAltSession(AltSession altSession) {
        this.altSession = altSession;
    }

    public Zoom getZoom() {
        return zoom;
    }

    public AuthenticationProviderMap getAuthenticationProviderMap() {
        return authenticationProviderMap;
    }

}
