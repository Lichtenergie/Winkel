package de.dietrichpaul.winkel.feature.command;

import com.mojang.brigadier.StringReader;
import de.dietrichpaul.winkel.event.EventDispatcher;
import de.dietrichpaul.winkel.event.list.ChatListener;
import de.dietrichpaul.winkel.event.list.MakePlayHandlerListener;

public class CommandListener implements ChatListener, MakePlayHandlerListener {

    private final CommandManager manager;
    private InternalCommandSource internalSource;

    public CommandListener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onChat(ChatEvent event) {
        String message = event.getMessage();
        if (!message.startsWith("#"))
            return;

        event.cancel();
        event.addToHistory();

        StringReader reader = new StringReader(message);
        reader.setCursor("#".length());

        this.manager.execute(reader, this.internalSource);
    }

    public void register(EventDispatcher events) {
        events.subscribe(ChatListener.class, this);
    }

    @Override
    public void onMakePlayHandler(MakePlayHandlerEvent event) {
        this.internalSource = new InternalCommandSource(event.getNetHandler());
    }

    public InternalCommandSource getInternalSource() {
        return internalSource;
    }

}
