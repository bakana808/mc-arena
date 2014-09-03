package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.ArenaTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class ArenaMode {

    public ArenaMode() {g_reset();}

    /**
     * The list of players in this gamemode
     */
    private List<ArenaPlayer> players = new ArrayList<>();

    /**
     * Runs at the creation of the room (pre-gamemode)
     */
    public abstract void g_reset();

    /**
     * Runs at the start of a round.
     */
	public abstract void g_round_start();

    /**
     * Runs at the end of a round.
     * If neither <code>winningTeam</code> or <code>winningPlayer</code> are null,
     * it should be interpreted as the player winning for the team.
     * @param winningTeam The team that won, or null if current gamemode is not team-based
     * @param winningPlayer The player that won, or null if current gamemode is team-based
     */
	public abstract void g_round_end(ArenaTeam winningTeam, ArenaPlayer winningPlayer);

    /**
     * Ends the gamemode abruptbly, with no winning team or player.
     * Equivalent to a stalemate.
     */
    public void g_round_end() {
        g_round_end(null, null);
    }

    /**
     * Ends the gamemode with a winning player.
     */
    public void g_round_end(ArenaPlayer winningPlayer) {
        g_round_end(null, winningPlayer);
    }

}
