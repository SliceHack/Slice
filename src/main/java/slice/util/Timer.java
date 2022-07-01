package slice.util;

/**
 * Utility class for timing.
 *
 * @author Nick
 * */
public class Timer {

    /** the last ms */
    public long lastMS = 0L;

    /**
     * Converts a delay to milliseconds.
     * */
    public int convertToMS(int d) {
        return 1000 / d;
    }

    /**
     * Gets the current time in milliseconds.
     * */
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    /**
     * Checks if the time has reached the specified delay.
     * */
    public boolean hasReached(long milliseconds) {
        return (getCurrentMS() - this.lastMS >= milliseconds);
    }

    /**
     * Checks if the time has reached the specified delay.
     * */
    public boolean hasTimeReached(long delay) {
        return (System.currentTimeMillis() - this.lastMS >= delay);
    }

    /**
     * Gets the delay.
     * */
    public long getDelay() {
        return System.currentTimeMillis() - this.lastMS;
    }

    /**
     * Resets the timer.
     * */
    public void reset() {
        this.lastMS = getCurrentMS();
    }

    /**
     * Sets the last time.
     * */
    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }

    /**
     * Sets the last time.
     * */
    public void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }



}
