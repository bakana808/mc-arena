package com.octopod.mgframe.game;

import com.octopod.mgframe.MGFPlayer;
import com.octopod.mgframe.MGFTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class MGFGame {

    public MGFGame() {g_reset();}

    /**
     * The list of players in this game
     */
    private List<MGFPlayer> players = new ArrayList<>();

    /**
     * Runs at the creation of the room (pre-game)
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
     * @param winningTeam The team that won, or null if current game is not team-based
     * @param winningPlayer The player that won, or null if current game is team-based
     */
	public abstract void g_round_end(MGFTeam winningTeam, MGFPlayer winningPlayer);

    /**
     * Ends the game abruptbly, with no winning team or player.
     * Equivalent to a stalemate.
     */
    public void g_round_end() {
        g_round_end(null, null);
    }

    /**
     * Ends the game with a winning player.
     */
    public void g_round_end(MGFPlayer winningPlayer) {
        g_round_end(null, winningPlayer);
    }

}
