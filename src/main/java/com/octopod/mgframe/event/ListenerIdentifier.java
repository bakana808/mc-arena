package com.octopod.mgframe.event;

/**
 * @author Octopod
 *         Created on 3/21/14
 */
public interface ListenerIdentifier {

    /**
     * Use this to resolve the type of listeners in the case
     * that their argument type is a Generic Type of Event.
     * @return The Class of the Event implementation.
     */
    public Class<? extends Event> getType();

}
