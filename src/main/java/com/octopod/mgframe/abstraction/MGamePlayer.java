package com.octopod.mgframe.abstraction;

import com.octopod.mgframe.MGFTeam;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class MGamePlayer {

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

    //=== No more abstract methods ===//

    /**
     * The weapons the player is holding
     */
    private MGameWeapon[] weapons = new MGameWeapon[7];

    /**
     * The room the player is in (null if not in any room)
     */
    private MGameRoom room = null;

    /**
     * The team the player is on
     */
    private MGFTeam team = null;

    public MGFTeam getTeam() {return team;}
    public MGameRoom getRoom() {return room;}

    public Object getHandle() {return null;}

    public MGameWeapon getHandWeapon() {
        return weapons[getHandSlot()];
    }

    /**
     * Sets the weapon association with a player's hotbar slot.
     * @param slot
     * @param weapon
     */
    public void setWeapon(int slot, MGameWeapon weapon) {
        weapons[slot] = weapon;
    }

    /**
     * Gives a player a weapon.
     * @param slot
     * @param weapon
     */
    public void giveWeapon(int slot, MGameWeapon weapon) {
        setWeapon(slot, weapon);
        giveItem(slot, weapon.getItemType(), weapon.getItemData(), weapon.getName(), null);
    }



}
