package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface DoAttackListener {

    void onDoAttack();

    class DoAttackEvent extends AbstractEvent<DoAttackListener> {

        public static final DoAttackEvent INSTANCE = new DoAttackEvent();
        private static final EventExecutor<DoAttackListener> EXECUTOR = DoAttackListener::onDoAttack;

        @Override
        public EventExecutor<DoAttackListener> getEventExecutor() {
            return EXECUTOR;
        }

        @Override
        public Class<DoAttackListener> getListenerType() {
            return DoAttackListener.class;
        }

    }

}
