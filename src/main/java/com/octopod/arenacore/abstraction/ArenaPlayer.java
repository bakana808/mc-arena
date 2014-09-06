package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.ArenaTeam;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class ArenaPlayer {

	public abstract void setExpBar(float exp);

	public abstract void setExpLevel(int level);

	public abstract void setMaxHealth(int health);

	public abstract void setHealth(int health);

	public abstract int getMaxHealth();

	public abstract int getHealth();

	public abstract void hurtDirectly(int damage);

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

	private float shield;
	private float maxShield;

	/**
	 * The class the player is currently using
	 */
	private ArenaClass clazz;

    /**
     * The weapons the player is holding
     */
    private ArenaWeapon[] weapons = new ArenaWeapon[7];

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

    public ArenaWeapon getHandWeapon() {
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
    public void setWeapon(int slot, ArenaWeapon weapon) {
        weapons[slot] = weapon;
    }

    /**
     * Gives a player a weapon.
     * @param slot
     */
    public void giveWeapon(int slot, ArenaWeaponScript script) {
		ArenaWeapon weapon = new ArenaWeapon(slot, this, script);
        setWeapon(slot, weapon);
        giveItem(slot, weapon.getItemType(), weapon.getItemData(), weapon.getDisplayName(), null);
    }

	public ArenaClass getPlayerClass() {
		return clazz;
	}

	public void setPlayerClass(ArenaClassScript script) {
		clazz = new ArenaClass(this, script);
		ArenaClassConfig config = clazz.getConfig();
		setMaxHealth(config.maxHealth);
		setHealth(config.health);
		setMaxShield(config.maxShield);
		setShield(config.shield);
		setWalkSpeed(config.walkSpeed);
		setCanFly(config.canFly);
		setHunger(config.canRun ? 20 : 3);
	}

	public void setShield(int shield) {
		this.shield = shield;
		setExpLevel(shield);
		setExpBar((float)getShield() / (float)getMaxShield());
	}

	public void setMaxShield(int shield) {
		this.maxShield = shield;
		setExpBar((float)getShield() / (float)getMaxShield());
	}

	public int getShield() {
		return (int)shield;
	}

	public int getMaxShield() {
		return (int)maxShield;
	}

	public void hurt(int damage) {
		if(getShield() >= damage) {
			//Run a shield-damage script
			setShield(getShield() - damage);
		} else
		if(getShield() == 0) {
			//Run a hurt-directly script
			hurtDirectly(damage);
		} else {
			//Run a shield-depleted script
			int leftover = damage - getShield();
			hurtDirectly(leftover);
		}
	}

}
