package de.dietrichpaul.winkel.event.list.render;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;
import net.minecraft.client.util.math.MatrixStack;

public interface RenderOverlayListener {

    void onRender(RenderOverlayEvent event);

    class RenderOverlayEvent extends AbstractEvent<RenderOverlayListener> {

        private final EventExecutor<RenderOverlayListener> executor = listener -> listener.onRender(this);
        private float delta;
        private MatrixStack matrices;

        public RenderOverlayEvent(float delta, MatrixStack matrices) {
            this.delta = delta;
            this.matrices = matrices;
        }

        @Override
        public EventExecutor<RenderOverlayListener> getEventExecutor() {
            return executor;
        }

        @Override
        public Class<RenderOverlayListener> getListenerType() {
            return RenderOverlayListener.class;
        }

        public float getDelta() {
            return delta;
        }

        public MatrixStack getMatrices() {
            return matrices;
        }

    }

}
