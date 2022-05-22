package de.dietrichpaul.winkel.event;

public abstract class BreakableAbstractEvent<L> extends AbstractEvent<L> {

    private boolean abort;

    public void stopHandling() {
        this.abort = true;
    }

    @Override
    public boolean isAbort() {
        return this.abort;
    }

}
