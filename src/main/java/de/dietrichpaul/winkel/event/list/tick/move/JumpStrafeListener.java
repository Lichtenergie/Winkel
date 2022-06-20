package de.dietrichpaul.winkel.event.list.tick.move;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface JumpStrafeListener {

    void onJumpStrafe(JumpStrafeEvent event);

    class JumpStrafeEvent extends BreakableAbstractEvent<JumpStrafeListener> {

        private float yaw;
        private EventExecutor<JumpStrafeListener> eventExecutor = listener -> listener.onJumpStrafe(this);

        public JumpStrafeEvent(float yaw) {
            this.yaw = yaw;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        @Override
        public EventExecutor<JumpStrafeListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<JumpStrafeListener> getListenerType() {
            return JumpStrafeListener.class;
        }

    }

}