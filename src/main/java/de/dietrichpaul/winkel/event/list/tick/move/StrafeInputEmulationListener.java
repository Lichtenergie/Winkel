package de.dietrichpaul.winkel.event.list.tick.move;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface StrafeInputEmulationListener {

    void onStrafeInputEmulation(StrafeInputEmulationEvent event);

    class StrafeInputEmulationEvent extends BreakableAbstractEvent<StrafeInputEmulationListener> {

        private final EventExecutor<StrafeInputEmulationListener> eventExecutor = listener -> listener.onStrafeInputEmulation(this);

        private float movementSideways;
        private float movementForward;
        private boolean slowdown;
        private float slowness;

        public StrafeInputEmulationEvent(float movementSideways, float movementForward, boolean slowdown, float slowness) {
            this.movementSideways = movementSideways;
            this.movementForward = movementForward;
            this.slowdown = slowdown;
            this.slowness = slowness;
        }

        public float getSlowness() {
            return slowness;
        }

        public void setSlowdown(boolean slowdown) {
            this.slowdown = slowdown;
        }

        public void setSlowness(float slowness) {
            this.slowness = slowness;
        }

        public boolean hasSlowdown() {
            return slowdown;
        }

        public float getMovementSideways() {
            return movementSideways;
        }

        public void setMovementSideways(float movementSideways) {
            this.movementSideways = movementSideways;
        }

        public float getMovementForward() {
            return movementForward;
        }

        public void setMovementForward(float movementForward) {
            this.movementForward = movementForward;
        }


        @Override
        public EventExecutor<StrafeInputEmulationListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<StrafeInputEmulationListener> getListenerType() {
            return StrafeInputEmulationListener.class;
        }

    }

}