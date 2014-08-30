package com.octopod.mgframe.abstraction;

import com.octopod.mgframe.abstraction.MGamePlayer;
import com.octopod.mgframe.MGFTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class MGameMode {

    public MGameMode() {g_reset();}

    /**
     * The list of players in this gamemode
     */
    private List<MGamePlayer> players = new ArrayList<>();

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
	public abstract void g_round_end(MGFTeam winningTeam, MGamePlayer winningPlayer);

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
    public void g_round_end(MGamePlayer winningPlayer) {
        g_round_end(null, winningPlayer);
    }

}
