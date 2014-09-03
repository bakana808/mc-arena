package com.octopod.arenacore.abstraction.bukkit;

import com.octopod.arenacore.abstraction.ArenaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitGamePlayer  extends ArenaPlayer {

    Player handle;

    public BukkitGamePlayer(Player player) {
        handle = player;
    }

    @Override
    public String getName() {return handle.getName();}

    @Override
    public String getID() {return handle.getUniqueId().toString();}

    @Override
    public Object getHandle() {return handle;}

    @Override
    public int getHandSlot() {
        return handle.getInventory().getHeldItemSlot();
    }

    @Override
    public void setHandSlot(int slot) {
        handle.getInventory().setHeldItemSlot(slot);
    }

    @Override
    protected void giveItem(int slot, int type, int data, String name, List<String> description) {
        ItemStack item = new ItemStack(type, 1, (short)data);
		ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if(description != null) meta.setLore(description);
		item.setItemMeta(meta);
        handle.getInventory().setItem(slot, item);
    }

	protected void renameItem(int slot, String name) {
		ItemStack item = handle.getInventory().getItem(slot);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}
}
