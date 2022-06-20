package de.dietrichpaul.winkel.event.list.rotationrequest;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface RequestSpoofedPrevPitchListener {

    void onRequestSpoofedPrevPitch(RequestSpoofedPrevPitchEvent event);

    class RequestSpoofedPrevPitchEvent extends BreakableAbstractEvent<RequestSpoofedPrevPitchListener> {

        private float pitch;
        private final EventExecutor<RequestSpoofedPrevPitchListener> eventExecutor = listener -> listener.onRequestSpoofedPrevPitch(this);

        public RequestSpoofedPrevPitchEvent(float pitch) {
            this.pitch = pitch;
        }

        @Override
        public EventExecutor<RequestSpoofedPrevPitchListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<RequestSpoofedPrevPitchListener> getListenerType() {
            return RequestSpoofedPrevPitchListener.class;
        }

        public void setPrevPitch(float pitch) {
            this.pitch = pitch;
        }

        public float getPrevPitch() {
            return pitch;
        }

    }

}