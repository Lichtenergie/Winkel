package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface InputHandleListener {

    void onHandleInput(InputHandleEvent event);

    class InputHandleEvent extends BreakableAbstractEvent<InputHandleListener> {

        public static final InputHandleEvent INSTANCE = new InputHandleEvent();

        private final EventExecutor<InputHandleListener> executor = listener -> listener.onHandleInput(this);

        @Override
        public EventExecutor<InputHandleListener> getEventExecutor() {
            return this.executor;
        }

        @Override
        public Class<InputHandleListener> getListenerType() {
            return InputHandleListener.class;
        }

    }

}
