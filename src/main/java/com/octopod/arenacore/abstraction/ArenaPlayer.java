package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.ArenaTeam;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class ArenaPlayer {

    protected abstract void giveItem(int slot, int type, int data, String name, List<String> description);

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
    public void giveWeapon(int slot, ArenaWeapon.Script script) {
		ArenaWeapon weapon = new ArenaWeapon(this, script);
        setWeapon(slot, weapon);
        giveItem(slot, weapon.getItemType(), weapon.getItemData(), weapon.getName(), null);
    }

}
