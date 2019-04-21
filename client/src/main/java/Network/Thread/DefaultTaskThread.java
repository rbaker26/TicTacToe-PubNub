package Network.Thread;

class DefaultTaskThread implements TaskThread {
    private InterruptibleCallback callback;
    private Thread thread;

    DefaultTaskThread(InterruptibleCallback callback) {
        this.callback = callback;
        thread = new Thread(callback);
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void stop() {
        callback.setAlive(false);
    }

    @Override
    public void join() throws InterruptedException {
        stop();
        thread.join();
    }

    @Override
    public boolean isAlive() {
        return callback.isAlive();
    }
}
