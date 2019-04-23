package TaskThreads;

/**
 * Deals with a single task on another thread.
 */
public interface TaskThread {

    static void meh() {

    }

    /**
     * Starts up the thread.
     */
    void start();

    /**
     * Causes the thread to terminate at some point in the future.
     */
    void stop();

    /**
     * Waits for the thread termination. Calling this should cause the thread to end at some point.
     * @throws InterruptedException If somehow we get interrupted while waiting for the thread to stop.
     */
    void join() throws InterruptedException;

    /**
     * Checks if the thread is alive. This is false if the thread hasn't started yet.
     * @return True if the thread is still going.
     */
    boolean isAlive();
}
