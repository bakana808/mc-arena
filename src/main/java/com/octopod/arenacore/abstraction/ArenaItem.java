package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.script.ArenaItemScript;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaItem {

	private ArenaPlayer owner;
	private int slot;

    /**
     * The interface this weapon will use when firing.
     */
	private ArenaItemScript script;

    /**
     * At what UNIX time they can fire their weapon (right-click)
     */
    private long nextFire = 0;

	public ArenaItem(int slot, ArenaPlayer owner, ArenaItemScript script)
	{
		this.slot = slot;
		this.owner = owner;
		this.script = script;
	}

	public ArenaPlayer getOwner() {return owner;}

    public void scriptSecondaryAttack() {
		if(canFire()) {
			script.leftClickEvent(owner);
			//rename(getDisplayName());
		}
    }

    public void scriptPrimaryAttack() {
		if(canFire()) {
			script.rightClickEvent(owner);
			//rename(getDisplayName());
		}
    }

	public boolean scriptDropWeapon() {
		return script.dropEvent(owner);
	}

	public boolean scriptPickupWeapon() {
		return script.dropEvent(owner);
	}

	public ArenaItemConfig getConfig() {
		return script.getConfig();
	}

//    public int getAmmo() {return ammo;}
//
//    public void addAmmo(int i) {
//		ammo += i;
//		rename(getDisplayName());
//	}
//    public void setAmmo(int i) {
//		ammo = Math.min(maxAmmo, i);
//		rename(getDisplayName());
//	}
//	public boolean hasAmmo() {return ammo == -1 || ammo > 0;}

    public String getName() {
        return getConfig().name;
    }

	public String getAuthor() {
		return getConfig().author;
	}

    public int getItemType() {
        return getConfig().item_type;
    }

    public int getItemData() {
        return getConfig().item_data;
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

	private void rename(String name) {
		owner.renameItem(slot, name);
	}
}