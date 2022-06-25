package de.dietrichpaul.winkel.event.list.render;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;

public interface MaxHealthListener {

    void onMaxHealth(MaxHealthEvent event);

    class MaxHealthEvent extends AbstractEvent<MaxHealthListener> {

        private final EventExecutor<MaxHealthListener> executor = listener -> listener.onMaxHealth(this);

        private float maxHealth;

        public MaxHealthEvent(float maxHealth) {
            this.maxHealth = maxHealth;
        }

        @Override
        public EventExecutor<MaxHealthListener> getEventExecutor() {
            return executor;
        }

        @Override
        public Class<MaxHealthListener> getListenerType() {
            return MaxHealthListener.class;
        }

        public float getMaxHealth() {
            return maxHealth;
        }

        public void setMaxHealth(float maxHealth) {
            this.maxHealth = maxHealth;
        }

    }

}
