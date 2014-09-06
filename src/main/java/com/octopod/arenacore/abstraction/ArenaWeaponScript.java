package com.octopod.arenacore.abstraction;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface ArenaWeaponScript {

	/**
	 * Gets the configuration of this script
	 * @return
	 */
	public ArenaWeaponConfig getConfig();

	/**
	 * Runs when the player left-clicks with this weapon equipped
	 * @param player
	 */
	public void secondaryAttack(ArenaPlayer player);

	/**
	 * Runs when the player right-clicks with this weapon equipped
	 * (Runs every tick when held down)
	 * @param player
	 */
	public void primaryAttack(ArenaPlayer player);

	/**
	 * Runs when the player picks up a weapon
	 * @param player
	 * @return
	 */
	public void pickupWeapon(ArenaPlayer player);

	/**
	 * Runs when the player drops their weapon
	 * @param player
	 * @return
	 */
	public void dropWeapon(ArenaPlayer player);

}
