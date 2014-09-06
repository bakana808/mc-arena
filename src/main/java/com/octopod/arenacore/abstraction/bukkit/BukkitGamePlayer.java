package com.octopod.arenacore.abstraction.bukkit;

import com.octopod.arenacore.abstraction.ArenaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitGamePlayer extends ArenaPlayer {

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
	public void setExpBar(float shield) {
		handle.setExp(shield);
	}

	@Override
	public void setExpLevel(int level) {
		handle.setLevel(level);
	}

	@Override
	public void setMaxHealth(int health) {
		handle.setMaxHealth(health);
	}

	@Override
	public void setHealth(int health) {
		handle.setHealth(health);
	}

	@Override
	public int getMaxHealth() {
		return (int)handle.getMaxHealth();
	}

	@Override
	public int getHealth() {
		return (int)handle.getHealth();
	}

	@Override
	public void hurtDirectly(int damage) {
		setHealth(getHealth() - damage);
	}

	@Override
	public void setWalkSpeed(float speed) {
		handle.setWalkSpeed(speed);
	}

	@Override
	public void setHunger(int hunger) {
		handle.setFoodLevel(hunger);
	}

	@Override
	public void setCanFly(boolean fly) {
		handle.setAllowFlight(fly);
	}

	@Override
	public void setFly(boolean fly) {
		handle.setFlying(fly);
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

	@Override
	public void renameItem(int slot, String name) {
		ItemStack item = handle.getInventory().getItem(slot);
		if(item != null) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			item.setItemMeta(meta);
		}
	}
}
