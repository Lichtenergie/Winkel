package de.dietrichpaul.winkel.event.list.rotationrequest;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface RequestSpoofedPitchListener {

    void onRequestSpoofedPitch(RequestSpoofedPitchEvent event);

    class RequestSpoofedPitchEvent extends BreakableAbstractEvent<RequestSpoofedPitchListener> {

        private float pitch;
        private final EventExecutor<RequestSpoofedPitchListener> eventExecutor = listener -> listener.onRequestSpoofedPitch(this);

        public RequestSpoofedPitchEvent(float pitch) {
            this.pitch = pitch;
        }

        @Override
        public EventExecutor<RequestSpoofedPitchListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<RequestSpoofedPitchListener> getListenerType() {
            return RequestSpoofedPitchListener.class;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public float getPitch() {
            return pitch;
        }

    }

}