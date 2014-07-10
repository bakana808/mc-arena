package com.octopod.mgframe.event;

/**
 * @author Octopod
 *         Created on 3/15/14
 */
public abstract class CancellableEvent extends Event {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
