package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.CancellableEvent;
import de.dietrichpaul.winkel.event.EventExecutor;
import net.minecraft.client.gui.hud.ChatHud;

public interface ChatListener {

    void onChat(ChatEvent event);

    class ChatEvent extends CancellableEvent<ChatListener> {

        private final EventExecutor<ChatListener> executor = listener -> listener.onChat(this);

        private String message;
        private ChatHud chatHud;

        public ChatEvent(String message, ChatHud chatHud) {
            this.message = message;
            this.chatHud = chatHud;
        }

        @Override
        public EventExecutor<ChatListener> getEventExecutor() {
            return this.executor;
        }

        @Override
        public Class<ChatListener> getListenerType() {
            return ChatListener.class;
        }

        public String getMessage() {
            return this.message;
        }

        public void addToHistory() {
            this.chatHud.addToMessageHistory(this.message);
        }

    }

}
