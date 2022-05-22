package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface UpdateListener {

    void onUpdate();

    class UpdateEvent extends AbstractEvent<UpdateListener> {

        public static final UpdateEvent INSTANCE = new UpdateEvent();
        private static final EventExecutor<UpdateListener> EXECUTOR = UpdateListener::onUpdate;

        @Override
        public EventExecutor<UpdateListener> getEventExecutor() {
            return EXECUTOR;
        }

        @Override
        public Class<UpdateListener> getListenerType() {
            return UpdateListener.class;
        }

    }

}
