package de.dietrichpaul.winkel.event.list;

import de.dietrichpaul.winkel.event.AbstractEvent;
import de.dietrichpaul.winkel.event.EventExecutor;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public interface MakePlayHandlerListener {

    void onMakePlayHandler(MakePlayHandlerEvent event);

    class MakePlayHandlerEvent extends AbstractEvent<MakePlayHandlerListener> {

        private final EventExecutor<MakePlayHandlerListener> executor = listener -> listener.onMakePlayHandler(this);

        private final ClientPlayNetworkHandler netHandler;

        public MakePlayHandlerEvent(ClientPlayNetworkHandler netHandler) {
            this.netHandler = netHandler;
        }

        public ClientPlayNetworkHandler getNetHandler() {
            return netHandler;
        }

        @Override
        public EventExecutor<MakePlayHandlerListener> getEventExecutor() {
            return this.executor;
        }

        @Override
        public Class<MakePlayHandlerListener> getListenerType() {
            return MakePlayHandlerListener.class;
        }

    }

}
