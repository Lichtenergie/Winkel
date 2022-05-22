package de.dietrichpaul.winkel.util;

public class Timer {

    private long previousTimeMillis;

    public void reset() {
        this.previousTimeMillis = System.currentTimeMillis();
    }

    public long getDelay() {
        return System.currentTimeMillis() - this.previousTimeMillis;
    }

    public boolean hasPassed(long millis) {
        return getDelay() >= millis;
    }

}
