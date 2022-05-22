package de.dietrichpaul.winkel;

import de.dietrichpaul.winkel.event.EventDispatcher;
import de.dietrichpaul.winkel.feature.Chat;
import de.dietrichpaul.winkel.feature.MacroList;
import de.dietrichpaul.winkel.feature.command.CommandManager;
import de.dietrichpaul.winkel.feature.hack.HackList;
import de.dietrichpaul.winkel.util.KeyboardMapper;

public class WinkelClient {

    public static final WinkelClient INSTANCE = new WinkelClient();

    private EventDispatcher eventDispatcher;
    private HackList hackList;
    private KeyboardMapper keyboardMapper;
    private CommandManager commandManager;
    private MacroList macroList;
    private Chat chat;

    public void init() {
        this.chat = new Chat();
        this.eventDispatcher = new EventDispatcher();
        this.hackList = new HackList();
        this.commandManager = new CommandManager();
        this.keyboardMapper = new KeyboardMapper();
        this.macroList = new MacroList();
    }

    public void start() {
        this.keyboardMapper.start();
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
