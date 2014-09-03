package com.octopod.arenacore;

import java.util.UUID;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface LoggerInterface {

    public void broadcast(String message);
    public void console(String message);
    public void player(UUID lookup, String message);

}
