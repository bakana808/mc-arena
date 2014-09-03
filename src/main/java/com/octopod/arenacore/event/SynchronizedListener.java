package com.octopod.arenacore.event;

public class SynchronizedListener<T extends Event> implements Listener<T>, ListenerIdentifier {

    private final Class<T> type;
    private Listener<T> listener;
	private final Object lock = new Object();

	public SynchronizedListener(Class<T> type, Listener<T> listener) {
		this.type = type;
        this.listener = listener;
	}

    /**
     * Implementation of ListenerIdentifier.
     * Because the below listener is a Generic type (Event) then we will use getType()
     * to getProperty the Event type instead.
     * @return
     */
    @Override
    public Class<T> getType() {return type;}

    /**
     * The synchronized listener. Runs the listener provided in the constructor,
     * and if the event is unlocked, notifies the lock.
     * @param event
     */
    @Override
    @EventHandler
	public void onEvent(T event) {
		try {
            listener.onEvent(event);
            if(event.isUnlocked()) {
                event.setUnlocked(false);
                synchronized(lock) {
                    executions++;
                    lock.notify();
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private int executions = 0;

    /**
     * Waits for the above listener to be fired X times, then returns.
     * Use timeout to timeout after X ms and return anyway.
     * @param timeout
     * @param expectedExecutions
     * @return
     */
	public SynchronizedListener<T> waitFor(long timeout, int expectedExecutions) {
		long startTime = System.currentTimeMillis();
        executions = 0;
		try {
			while(executions < expectedExecutions) {
				if((System.currentTimeMillis() - startTime) > timeout)
					break;
				synchronized(lock) {

					lock.wait(timeout);
				}
			}
		} catch (InterruptedException e) {}
		return this;
	}

	public SynchronizedListener<T> waitFor(long timeout) {
		return waitFor(timeout, 1);
	}

}
