package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface FreezeRotationUpdateListener {

    void onFreezeRotationUpdate(FreezeRotationUpdateEvent event);

    class FreezeRotationUpdateEvent extends BreakableAbstractEvent<FreezeRotationUpdateListener> {

        private boolean frozen;

        private final EventExecutor<FreezeRotationUpdateListener> eventExecutor = listener -> listener.onFreezeRotationUpdate(this);

        public boolean isFrozen() {
            return frozen;
        }

        public void setFrozen(boolean frozen) {
            this.frozen = frozen;
        }

        @Override
        public EventExecutor<FreezeRotationUpdateListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<FreezeRotationUpdateListener> getListenerType() {
            return FreezeRotationUpdateListener.class;
        }
    }

}