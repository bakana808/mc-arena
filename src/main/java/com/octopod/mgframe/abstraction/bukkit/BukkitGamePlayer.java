package com.octopod.mgframe.abstraction.bukkit;

import com.octopod.mgframe.abstraction.MGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitGamePlayer  extends MGamePlayer {

    Player handle;

    public BukkitGamePlayer(Player player) {
        handle = player;
    }

    @Override
    public Object getHandle() {
        return handle;
    }

    @Override
    public int getHandSlot() {
        return handle.getInventory().getHeldItemSlot();
    }

    @Override
    public void setHandSlot(int slot) {
        return handle.getInventory().setHeldItemSlot(slot);
    }

    @Override
    protected void giveItem(int slot, int type, int data, String name, List<String> description) {
        ItemStack item = new ItemStack(type, 1, (short)data);
        item.getItemMeta().setDisplayName(name);
        if(description != null)
            item.getItemMeta().setLore(description);
        handle.getInventory().setItem(slot, item);
    }
}
