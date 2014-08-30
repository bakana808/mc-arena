package com.octopod.mgframe.abstraction;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGameWeapon {

    private int ammo = 0;
    private MGameWeaponScript script;

    public MGameWeapon(MGameWeaponScript script) {
        this.script = script;
    }

    public void left_click(MGamePlayer player) {
        script.left_click(player);
    }

    public void right_click(MGamePlayer player) {
        script.right_click(player);
    }

    public int getAmmo() {return ammo;}
    public void addAmmo(int i) {ammo += i;}
    public void setAmmo(int i) {ammo = i;}

    public String getName() {
        return "[UNKNOWN WEAPON]";
    }

    public int getItemType() {
        return 1;
    }

    public int getItemData() {
        return 0;
    }




}
