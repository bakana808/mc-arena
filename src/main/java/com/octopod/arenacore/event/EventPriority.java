package com.octopod.arenacore.event;

/**
 * @author Octopod
 *         Created on 3/12/14
 */
public enum EventPriority {

    LOWEST (0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4),
    MONITOR(5);

    Integer n;
    private EventPriority(int n) {this.n = n;}

    public int getPriority() {return n;}

}
