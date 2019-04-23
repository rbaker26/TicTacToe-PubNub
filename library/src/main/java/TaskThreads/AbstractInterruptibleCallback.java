package TaskThreads;

import java.util.Objects;

/**
 * This is a simplistic implementation of the InterruptibleCallback. It periodically fires off the abstract
 * update method. Extenders only need to override this method.
 */
public abstract class AbstractInterruptibleCallback implements InterruptibleCallback {

    private boolean alive;
    private long period;

    /**
     * Constructs an instance of the callback, also setting the cycle period.
     * @param period The update period, in milliseconds.
     */
    protected AbstractInterruptibleCallback(long period) {
        setAlive(false);
        this.period = period;
    }

    /**
     * This gets called periodically. Each call is approximately separated by the period.
     */
    protected abstract void update();

    @Override
    public void run() {
        setAlive(true);

        while(isAlive()) {
            update();

            try {
                Thread.sleep(getPeriod());
            }
            catch(InterruptedException ex) {
                System.out.println("Heartbeat interrupted; dying");
                setAlive(false);
            }
        }
    }

    //region Getters and setters
    @Override
    public synchronized boolean isAlive() {
        return alive;
    }

    @Override
    public synchronized void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Gets the beat period in milliseconds.
     * @return Milliseconds.
     */
    public synchronized long getPeriod() {
        return period;
    }

    /**
     * Sets the period in milliseconds. This amount of time will be taken between beats.
     * @param period Period in milliseconds.
     */
    public synchronized void setPeriod(long period) {
        this.period = period;
    }
    //endregion


    //region Object overrides
    @Override
    public String toString() {
        return "AbstractInterruptibleCallback{" +
                "alive=" + alive +
                ", period=" + period +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractInterruptibleCallback that = (AbstractInterruptibleCallback) o;
        return isAlive() == that.isAlive() &&
                getPeriod() == that.getPeriod();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAlive(), getPeriod());
    }
    //endregion

}
