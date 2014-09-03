package com.octopod.arenacore.event;

/**
 * @author Octopod
 *         Created on 3/21/14
 */
public abstract class SynchronizableEvent {

    private boolean unlock = false;

    public void setUnlocked(boolean n) {
        unlock = n;
    }

    public boolean isUnlocked() {
        return unlock;
    }

}
