package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.chatbuilder.ChatUtils;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaWeapon {

	private ArenaPlayer owner;
	private int slot;

    /**
     * The interface this weapon will use when firing.
     */
	private ArenaWeaponScript script;

	private int maxAmmo;
    private int ammo;

    /**
     * At what UNIX time they can fire their weapon (right-click)
     */
    private long nextFire = 0;

    public ArenaWeapon(int slot, ArenaPlayer owner, ArenaWeaponScript script) {
		this(slot, owner, script, null);
    }

	public ArenaWeapon(int slot, ArenaPlayer owner, ArenaWeaponScript script, Integer ammo) {
		this.slot = slot;
		this.owner = owner;
		this.script = script;

		this.maxAmmo = getConfig().maxAmmo;
		//Set ammo to configured amount if null, else set to provided ammo amount
		setAmmo(ammo == null ? getConfig().ammo : ammo);
	}

	public ArenaPlayer getOwner() {return owner;}

    public void scriptSecondaryAttack() {
		if(canFire()) {
			script.secondaryAttack(owner);
			rename(getDisplayName());
		}
    }

    public void scriptPrimaryAttack() {
		if(canFire()) {
			script.primaryAttack(owner);
			rename(getDisplayName());
		}
    }

	public void scriptDropWeapon() {
		script.dropWeapon(owner);
	}

	public ArenaWeaponConfig getConfig() {
		return script.getConfig();
	}

    public int getAmmo() {return ammo;}

    public void addAmmo(int i) {
		ammo += i;
		rename(getDisplayName());
	}
    public void setAmmo(int i) {
		ammo = Math.min(maxAmmo, i);
		rename(getDisplayName());
	}
	public boolean hasAmmo() {return ammo == -1 || ammo > 0;}

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
		return ammo > 0 && System.currentTimeMillis() >= nextFire;
	}

	/**
	 * Sets the time for when the player can fire again.
	 * @param time
	 */
	public void setNextFire(long time) {
		nextFire = time;
	}

	public String getDisplayName() {
		String sAmmo = ammo == -1 ? "INF" : ammo + "/" + maxAmmo;
		return ChatUtils.colorize("&8--[ &f" + getName() + " &8| &f" + sAmmo + " &8]--");
	}
	private void rename(String name) {
		owner.renameItem(slot, name);
	}
}