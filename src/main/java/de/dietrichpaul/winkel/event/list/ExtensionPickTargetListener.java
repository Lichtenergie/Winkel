package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.BreakableAbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;
import net.minecraft.entity.LivingEntity;

public interface ExtensionPickTargetListener {

    void onWTapPickTarget(ExtensionPickTargetEvent event);

    class ExtensionPickTargetEvent extends BreakableAbstractEvent<ExtensionPickTargetListener> {

        private LivingEntity target;

        private EventExecutor<ExtensionPickTargetListener> eventExecutor = listener -> listener.onWTapPickTarget(this);

        public ExtensionPickTargetEvent(LivingEntity target) {
            this.target = target;
        }

        public LivingEntity getTarget() {
            return target;
        }

        public void setTarget(LivingEntity target) {
            this.target = target;
        }

        @Override
        public EventExecutor<ExtensionPickTargetListener> getEventExecutor() {
            return eventExecutor;
        }

        @Override
        public Class<ExtensionPickTargetListener> getListenerType() {
            return ExtensionPickTargetListener.class;
        }

    }

}