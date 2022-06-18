package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface GameTickListener {

    void onTick();

    class GameTickEvent extends AbstractEvent<GameTickListener> {

        private static final EventExecutor<GameTickListener> EXECUTOR = GameTickListener::onTick;
        public static final GameTickEvent INSTANCE = new GameTickEvent();

        @Override
        public EventExecutor<GameTickListener> getEventExecutor() {
            return EXECUTOR;
        }

        @Override
        public Class<GameTickListener> getListenerType() {
            return GameTickListener.class;
        }

    }

}
