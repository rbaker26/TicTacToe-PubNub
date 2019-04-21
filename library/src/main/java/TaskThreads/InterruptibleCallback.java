package TaskThreads;

/**
 * This is a runnable which is meant to be run in a thread, and has an alive property.
 * Implementers should exit gracefully upon having setAlive be set to false.
 */
public interface InterruptibleCallback extends Runnable {

    /**
     * Makes the callback either alive or dead.
     * @param alive Whether this will be alive or not.
     */
    void setAlive(boolean alive);

    /**
     * Returns the livingness(?) of the thread.
     * @return True if alive, false otherwise.
     */
    boolean isAlive();
}
