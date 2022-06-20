package de.dietrichpaul.winkel.event.list.rotationrequest;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface ForceSpoofRotationListener {

    void onForceSpoofRotation(ForceSpoofRotationEvent event);

    class ForceSpoofRotationEvent extends AbstractEvent<ForceSpoofRotationListener> {

        private float yaw;
        private float pitch;

        private boolean hasYaw;
        private boolean hasPitch;

        private EventExecutor<ForceSpoofRotationListener> eventExecutor = listener -> listener.onForceSpoofRotation(this);

        public ForceSpoofRotationEvent(float yaw, float pitch, boolean hasYaw, boolean hasPitch) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.hasYaw = hasYaw;
            this.hasPitch = hasPitch;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public boolean hasYaw() {
            return hasYaw;
        }

        public boolean hasPitch() {
            return hasPitch;
        }

        @Override
        public EventExecutor<ForceSpoofRotationListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<ForceSpoofRotationListener> getListenerType() {
            return ForceSpoofRotationListener.class;
        }

    }


}