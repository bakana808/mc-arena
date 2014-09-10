package com.octopod.arenacore.script;

import com.octopod.arenacore.abstraction.ArenaItemConfig;
import com.octopod.arenacore.abstraction.ArenaPlayer;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ArenaItemScript {

	/**
	 * Gets the configuration of this script
	 * @return
	 */
	public ArenaItemConfig getConfig();

	/**
	 * Runs when the player left-clicks with this weapon equipped
	 * @param player
	 */
	public void leftClickEvent(ArenaPlayer player);

	/**
	 * Runs when the player right-clicks with this weapon equipped
	 * (Runs every tick when held down)
	 * @param player
	 */
	public void rightClickEvent(ArenaPlayer player);

	/**
	 * Runs when the player picks up a weapon
	 * @param player
	 * @return
	 */
	public boolean pickupEvent(ArenaPlayer player);

	/**
	 * Runs when the player drops their weapon
	 * @param player
	 * @return
	 */
	public boolean dropEvent(ArenaPlayer player);

}
