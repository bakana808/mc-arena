package com.octopod.arenacore.abstraction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaWeaponConfig {

	/**
	 * The name of the weapon
	 */
	public String name = "Weapon";

	/**
	 * Who made the weapon
	 */
	public String author = "[UNKNOWN AUTHOR]";

	/**
	 * The description of the weapon
	 */
	public List<String> description = new ArrayList<>();

	/**
	 * The type (item ID) of the weapon
	 */
	public int itemType = 1;

	/**
	 * The data (damage value) of the weapon
	 */
	public int itemData = 0;

	/**
	 * The max amount of ammo this weapon can hold
	 * -1 for infinite
	 */
	public int maxAmmo = 20;

	/**
	 * The amount of ammo this weapon starts with
	 * -1 for infinite
	 */
	public int ammo = 20;

	/**
	 * Can the player drop this weapon?
	 */
	public boolean canDrop = true;

}
