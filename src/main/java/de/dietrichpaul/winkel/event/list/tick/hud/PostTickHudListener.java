package de.dietrichpaul.winkel.event.list.tick.hud;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface PostTickHudListener {

    void onPostTickHud();

    class PostTickHudEvent extends AbstractEvent<PostTickHudListener> {

        private static final EventExecutor<PostTickHudListener> EVENT_EXECUTOR = PostTickHudListener::onPostTickHud;
        public static final PostTickHudEvent INSTANCE = new PostTickHudEvent();

        @Override
        public EventExecutor<PostTickHudListener> getEventExecutor() {
            return EVENT_EXECUTOR;
        }

        @Override
        public Class<PostTickHudListener> getListenerType() {
            return PostTickHudListener.class;
        }

    }

}
