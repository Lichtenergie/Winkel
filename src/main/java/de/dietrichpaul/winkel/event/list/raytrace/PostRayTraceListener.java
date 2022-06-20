package de.dietrichpaul.winkel.event.list.raytrace;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface PostRayTraceListener {

    void onPostRayTrace(PostRayTraceEvent event);

    class PostRayTraceEvent extends BreakableAbstractEvent<PostRayTraceListener> {

        private final EventExecutor<PostRayTraceListener> eventExecutor = listener -> listener.onPostRayTrace(this);

        @Override
        public EventExecutor<PostRayTraceListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<PostRayTraceListener> getListenerType() {
            return PostRayTraceListener.class;
        }

    }

}