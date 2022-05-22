package de.dietrichpaul.winkel;

import de.dietrichpaul.winkel.event.EventDispatcher;
import de.dietrichpaul.winkel.feature.Chat;
import de.dietrichpaul.winkel.feature.FriendManager;
import de.dietrichpaul.winkel.feature.MacroList;
import de.dietrichpaul.winkel.feature.command.CommandManager;
import de.dietrichpaul.winkel.feature.hack.HackList;
import de.dietrichpaul.winkel.util.KeyboardMapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.MacWindowUtil;
import net.minecraft.client.util.Window;

public class WinkelClient {
    public static final WinkelClient INSTANCE = new WinkelClient();

    public static final String NAME = "Winkel";
    public static final String VERSION = "2.0.0-release.1";

    private EventDispatcher eventDispatcher;
    private HackList hackList;
    private KeyboardMapper keyboardMapper;
    private CommandManager commandManager;
    private MacroList macroList;
    private Chat chat;

    private FriendManager friendManager;

    public void init() {
        this.chat = new Chat();
        this.eventDispatcher = new EventDispatcher();
        this.hackList = new HackList();

        /* managers */
        this.commandManager = new CommandManager();
        this.friendManager = new FriendManager();

        this.keyboardMapper = new KeyboardMapper();
        this.macroList = new MacroList();
    }

    public void start() {
        this.keyboardMapper.start();
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
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
}
