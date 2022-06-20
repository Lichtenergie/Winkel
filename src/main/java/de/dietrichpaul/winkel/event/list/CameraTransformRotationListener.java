package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface CameraTransformRotationListener {

    void onCameraTransform(CameraTransformRotationEvent event);

    class CameraTransformRotationEvent extends BreakableAbstractEvent<CameraTransformRotationListener> {

        private float interpolatedYaw;
        private float interpolatedPitch;
        private final float tickDelta;

        private EventExecutor<CameraTransformRotationListener> eventExecutor = listener -> listener.onCameraTransform(this);

        public CameraTransformRotationEvent(float interpolatedYaw, float interpolatedPitch, float tickDelta) {
            this.interpolatedYaw = interpolatedYaw;
            this.interpolatedPitch = interpolatedPitch;
            this.tickDelta = tickDelta;
        }

        public float getTickDelta() {
            return tickDelta;
        }

        public float getInterpolatedYaw() {
            return interpolatedYaw;
        }

        public void setInterpolatedYaw(float interpolatedYaw) {
            this.interpolatedYaw = interpolatedYaw;
        }

        public float getInterpolatedPitch() {
            return interpolatedPitch;
        }

        public void setInterpolatedPitch(float interpolatedPitch) {
            this.interpolatedPitch = interpolatedPitch;
        }

        @Override
        public EventExecutor<CameraTransformRotationListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<CameraTransformRotationListener> getListenerType() {
            return CameraTransformRotationListener.class;
        }

    }

}