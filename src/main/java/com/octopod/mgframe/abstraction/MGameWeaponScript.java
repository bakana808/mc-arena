package com.octopod.mgframe.abstraction;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface MGameWeaponScript {

    /**
     * Runs when the player left-clicks with this weapon equipped
     * @param player
     */
    public void left_click(MGamePlayer player);

    /**
     * Runs when the player right-clicks with this weapon equipped
     * (Runs every tick when held down)
     * @param player
     */
    public void right_click(MGamePlayer player);

}
