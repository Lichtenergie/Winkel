package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.CancellableEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface KeyInputListener {

    void onInput(KeyInputEvent event);

    class KeyInputEvent extends CancellableEvent<KeyInputListener> {

        private final EventExecutor<KeyInputListener> EXECUTOR = listener -> listener.onInput(this);

        private int key;
        private int scan;
        private int action;
        private int modifiers;

        public KeyInputEvent(int key, int scan, int action, int modifiers) {
            this.key = key;
            this.scan = scan;
            this.action = action;
            this.modifiers = modifiers;
        }

        @Override
        public EventExecutor<KeyInputListener> getEventExecutor() {
            return EXECUTOR;
        }

        @Override
        public Class<KeyInputListener> getListenerType() {
            return KeyInputListener.class;
        }

        public int getKey() {
            return key;
        }

        public int getScan() {
            return scan;
        }

        public int getAction() {
            return action;
        }

        public int getModifiers() {
            return modifiers;
        }
    }

}
