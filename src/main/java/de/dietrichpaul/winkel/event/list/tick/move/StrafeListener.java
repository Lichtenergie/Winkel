package de.dietrichpaul.winkel.event.list.tick.move;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface StrafeListener {

    void onStrafe(StrafeEvent event);

    class StrafeEvent extends BreakableAbstractEvent<StrafeListener> {

        private float yaw;

        private final EventExecutor<StrafeListener> eventExecutor = listener -> listener.onStrafe(this);

        public StrafeEvent(float yaw) {
            this.yaw = yaw;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        @Override
        public EventExecutor<StrafeListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<StrafeListener> getListenerType() {
            return StrafeListener.class;
        }

    }

}