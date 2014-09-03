package com.octopod.arenacore.abstraction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaWeapon {

	public static class Config
	{
		public String name = "[UNKNOWN WEAPON]";
		public String author = "[UNKNOWN AUTHOR]";
		public List<String> description = new ArrayList<>();
		public int itemType = 1;
		public int itemData = 0;
	}

	public static interface Script
	{
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
		 * Runs when the player drops their weapon
		 * @param player
		 * @return
		 */
		public void dropWeapon(ArenaPlayer player);

		public ArenaWeapon.Config getConfig(ArenaWeapon.Config defaults);
	}

	private ArenaPlayer owner;

    /**
     * The interface this weapon will use when firing.
     */
	private ArenaWeapon.Script script;

	private ArenaWeapon.Config config = null;

    private int ammo = 0;

    /**
     * At what UNIX time they can fire their weapon (right-click)
     */
    private long nextFire = 0;

    public ArenaWeapon(ArenaPlayer owner, ArenaWeapon.Script script) {
		this.owner = owner;
        this.script = script;
    }

	public ArenaPlayer getOwner() {return owner;}

    public void secondaryAttack() {
		if(canFire())
        script.secondaryAttack(owner);
    }

    public void primaryAttack() {
		if(canFire())
        script.primaryAttack(owner);
    }

	public ArenaWeapon.Config initializeConfig(ArenaWeapon.Config defaults) {
		return script.getConfig(defaults);
	}

	public ArenaWeapon.Config getConfig() {
		if(config == null) return config = initializeConfig(new ArenaWeapon.Config());
		return config;
	}

    public int getAmmo() {return ammo;}
    public void addAmmo(int i) {ammo += i;}
    public void setAmmo(int i) {ammo = i;}

    public String getName() {
        return getConfig().name;
    }

	public String getAuthor() {
		return getConfig().author;
	}

	public List<String> getDescription() {
		return getConfig().description;
	}

    public int getItemType() {
        return getConfig().itemType;
    }

    public int getItemData() {
        return getConfig().itemData;
    }

	/**
	 * Can the player fire? (the current time is higher than the time they can fire)
	 * @return
	 */
	public boolean canFire() {
		return System.currentTimeMillis() >= nextFire;
	}

	/**
	 * Sets the time for when the player can fire again.
	 * @param time
	 */
	public void setNextFire(long time) {
		nextFire = time;
	}
}