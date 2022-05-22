package de.dietrichpaul.winkel.event;

public abstract class AbstractEvent<L> {

    public abstract EventExecutor<L> getEventExecutor();

    public boolean isAbort() {
        return false;
    }

    public abstract Class<L> getListenerType();

}
