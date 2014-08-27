package com.octopod.mgframe.room;

import com.octopod.mgframe.MGFPlayer;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class MGFRoom {

    public MGFRoom() {r_reset();}

    public abstract void r_reset();

    public abstract void r_player_bring(MGFPlayer player);

    public abstract void r_player_kick(MGFPlayer player, String reason);

    /**
     *  Changes the map. Doesn't work if the world is already in use by another room.
     *  @param name the name of the world
     */
    public abstract void r_map(String name);

    /**
     *  Shuts down the room and sends all players back to the lobby (?)
     *  or kicks them off the server.
     */
    public void r_shutdown() {

    }

    /**
     *
     */

}
