package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.ArenaConfiguration;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaItemConfig implements ArenaConfiguration
{

	/**
	 * The name of the weapon
	 */
	public String name = "Weapon";

	/**
	 * Who made the weapon
	 */
	public String author = "[UNKNOWN AUTHOR]";

	/**
	 * The type (item ID) of the weapon
	 */
	public int item_type = 1;

	/**
	 * The data (damage value) of the weapon
	 */
	public int item_data = 0;

	/**
	 * Can the player drop this weapon?
	 */
	public boolean can_drop = true;

}
