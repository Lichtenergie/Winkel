package de.dietrichpaul.winkel;

import de.dietrichpaul.winkel.config.ConfigManager;
import de.dietrichpaul.winkel.event.EventDispatcher;
import de.dietrichpaul.winkel.feature.Chat;
import de.dietrichpaul.winkel.feature.FriendManager;
import de.dietrichpaul.winkel.feature.MacroList;
import de.dietrichpaul.winkel.feature.Zoom;
import de.dietrichpaul.winkel.feature.alt.AltSession;
import de.dietrichpaul.winkel.feature.alt.AuthenticationProviderMap;
import de.dietrichpaul.winkel.feature.alt.SessionAdapter;
import de.dietrichpaul.winkel.feature.command.CommandManager;
import de.dietrichpaul.winkel.feature.hack.HackList;
import de.dietrichpaul.winkel.feature.hack.engine.click.ClickEngine;
import de.dietrichpaul.winkel.feature.pattern.click.ClickPatternMap;
import de.dietrichpaul.winkel.feature.pattern.rotation.RotationPatternMap;
import de.dietrichpaul.winkel.injection.accessor.client.IMinecraftClientMixin;
import de.dietrichpaul.winkel.property.PropertyMap;
import de.dietrichpaul.winkel.util.keyboard.KeyboardMapper;
import net.minecraft.client.MinecraftClient;

import java.io.File;

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
    private PropertyMap propertyMap;
    private Chat chat;
    private ClickPatternMap clickPatternMap;
    private Zoom zoom;
    private AuthenticationProviderMap authenticationProviderMap;
    private ClickEngine clickEngine;
    private ConfigManager configManager;
    private RotationPatternMap rotationPatternMap;

    private File directory;

    private AltSession altSession;

    public void init() {
        this.directory = new File(MinecraftClient.getInstance().runDirectory, "Winkel");
        if (!this.directory.isDirectory())
            this.directory.mkdir();
        this.propertyMap = new PropertyMap();
        this.clickPatternMap = new ClickPatternMap();
        this.rotationPatternMap = new RotationPatternMap();
        this.chat = new Chat();
        this.authenticationProviderMap = new AuthenticationProviderMap();
        this.eventDispatcher = new EventDispatcher();
        this.hackList = new HackList();
        this.commandManager = new CommandManager();
        this.keyboardMapper = new KeyboardMapper();
        this.friendManager = new FriendManager();
        this.macroList = new MacroList();
        this.zoom = new Zoom();
        this.clickEngine = new ClickEngine();
        this.configManager = new ConfigManager();
    }

    public void start() {
        this.keyboardMapper.start();
        this.configManager.start();

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

    public PropertyMap getPropertyMap() {
        return propertyMap;
    }

    public File getDirectory() {
        return directory;
    }

    public ClickEngine getClickEngine() {
        return clickEngine;
    }

    public ClickPatternMap getClickPatternMap() {
        return clickPatternMap;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public RotationPatternMap getRotationPatternMap() {
        return rotationPatternMap;
    }

}
