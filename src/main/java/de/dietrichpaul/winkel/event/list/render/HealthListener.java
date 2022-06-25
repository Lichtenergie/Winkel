package de.dietrichpaul.winkel.event.list.render;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface HealthListener {

    void onHealth(HealthEvent event);

    class HealthEvent extends AbstractEvent<HealthListener> {

        private final EventExecutor<HealthListener> executor = listener -> listener.onHealth(this);

        private float health;

        public HealthEvent(float health) {
            this.health = health;
        }

        @Override
        public EventExecutor<HealthListener> getEventExecutor() {
            return executor;
        }

        @Override
        public Class<HealthListener> getListenerType() {
            return HealthListener.class;
        }

        public float getHealth() {
            return health;
        }

        public void setHealth(float health) {
            this.health = health;
        }

    }

}
