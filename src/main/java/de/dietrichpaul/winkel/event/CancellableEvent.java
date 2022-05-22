package de.dietrichpaul.winkel.event;

public abstract class CancellableEvent<L> extends AbstractEvent<L> {

    private boolean cancelled;

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
