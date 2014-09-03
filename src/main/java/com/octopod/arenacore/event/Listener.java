package com.octopod.arenacore.event;

/**
 * @author Octopod
 *         Last updated on 3/7/14
 */
public interface Listener<T extends Event> {

    public void onEvent(T event);

}
