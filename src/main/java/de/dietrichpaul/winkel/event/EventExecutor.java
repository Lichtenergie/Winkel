package de.dietrichpaul.winkel.event;

public interface EventExecutor<L> {

    void execute(L listener);

}
