package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.ArenaTeam;
import com.octopod.arenacore.Vector;
import com.octopod.arenacore.script.ArenaClassScript;
import com.octopod.arenacore.script.ArenaItemScript;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class ArenaPlayer {

	public abstract Vector forward(double offsetYaw, double offsetPitch);

	public abstract Vector loc();

	public abstract void setExpBar(float exp);

	public abstract void setExpLevel(int level);

	public abstract void setMaxHealth(int health);

	public abstract void setHealth(int health);

	public abstract int getMaxHealth();

	public abstract int getHealth();

	public abstract void hurt(int damage);

	public abstract void setWalkSpeed(float speed);

	public abstract void setHunger(int hunger);

	public abstract void setCanFly(boolean fly);

	public abstract void setFly(boolean fly);

    protected abstract void giveItem(int slot, int type, int data, String name, List<String> description);

	public abstract void renameItem(int slot, String name);

    /**
     * Gets the ID of the slot the player currently has selected
     * @return
     */
    public abstract int getHandSlot();

    /**
     * Sets the current slot the player is selecting
     */
    public abstract void setHandSlot(int slot);

    public abstract String getName();

    public abstract String getID();

    //=== No more abstract methods ===//

	public Vector forward()
	{
		return forward(0, 0);
	}

	public Vector head()
	{
		return loc().add(0, 2.8, 0);
	}

	public Vector eyes()
	{
		return loc().add(0, 2.62, 0);
	}

	private float shield;
	private float maxShield;

	/**
	 * The class the player is currently using
	 */
	private ArenaClass clazz;

    /**
     * The weapons the player is holding
     */
    private ArenaItem[] weapons = new ArenaItem[7];

    /**
     * The room the player is in (null if not in any room)
     */
    private ArenaRoom room = null;

    /**
     * The team the player is on
     */
    private ArenaTeam team = null;

    public ArenaTeam getTeam() {return team;}
    public ArenaRoom getRoom() {return room;}

    public Object getHandle() {return null;}

    public ArenaItem getHandWeapon() {
        return weapons[getHandSlot()];
    }

	public boolean hasWeaponInHand() {
		return getHandWeapon() != null;
	}

    /**
     * Sets the weapon association with a player's hotbar slot.
     * @param slot
     * @param weapon
     */
    public void setWeapon(int slot, ArenaItem weapon) {
        weapons[slot] = weapon;
    }

    /**
     * Gives a player a weapon.
     * @param slot
     */
    public void giveWeapon(int slot, ArenaItemScript script) {
		ArenaItem weapon = new ArenaItem(slot, this, script);
        setWeapon(slot, weapon);
        giveItem(slot, weapon.getItemType(), weapon.getItemData(), weapon.getName(), null);
    }

	public ArenaClass getPlayerClass() {
		return clazz;
	}

	public void setPlayerClass(ArenaClassScript script) {
		clazz = new ArenaClass(this, script);
		ArenaClassConfig config = clazz.getConfig();
		setMaxHealth(config.health_max);
		setHealth(config.health);
		setWalkSpeed(config.walk_speed);
		setCanFly(config.flying);
		setHunger(config.food);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ArenaPlayer && ((ArenaPlayer)obj).getID().equals(getID());
	}

}
