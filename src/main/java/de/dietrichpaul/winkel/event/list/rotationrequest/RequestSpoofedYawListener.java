package de.dietrichpaul.winkel.event.list.rotationrequest;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface RequestSpoofedYawListener {

    void onRequestSpoofedYaw(RequestSpoofedYawEvent event);

    class RequestSpoofedYawEvent extends BreakableAbstractEvent<RequestSpoofedYawListener> {

        private float yaw;
        private final EventExecutor<RequestSpoofedYawListener> eventExecutor = listener -> listener.onRequestSpoofedYaw(this);

        public RequestSpoofedYawEvent(float yaw) {
            this.yaw = yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        @Override
        public EventExecutor<RequestSpoofedYawListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<RequestSpoofedYawListener> getListenerType() {
            return RequestSpoofedYawListener.class;
        }

        public float getYaw() {
            return yaw;
        }

    }

}