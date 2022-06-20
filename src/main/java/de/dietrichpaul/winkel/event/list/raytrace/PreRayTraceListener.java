package de.dietrichpaul.winkel.event.list.raytrace;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface PreRayTraceListener {

    void onPreRayTrace();

    class PreRayTraceEvent extends AbstractEvent<PreRayTraceListener> {

        public static final PreRayTraceEvent INSTANCE = new PreRayTraceEvent();

        private static final EventExecutor<PreRayTraceListener> EVENT_EXECUTOR = PreRayTraceListener::onPreRayTrace;

        @Override
        public EventExecutor<PreRayTraceListener> getEventExecutor() {
            return EVENT_EXECUTOR;
        }

        @Override
        public Class<PreRayTraceListener> getListenerType() {
            return PreRayTraceListener.class;
        }

    }

}