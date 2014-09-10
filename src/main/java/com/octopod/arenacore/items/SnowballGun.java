package com.octopod.arenacore.items;

import com.octopod.arenacore.ProjectileUtil;
import com.octopod.arenacore.abstraction.ArenaItemConfig;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.script.ArenaItemScript;
import org.bukkit.entity.Snowball;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class SnowballGun implements ArenaItemScript {

	ArenaItemConfig config;

	public SnowballGun()
	{
		config = new ArenaItemConfig();
		config.item_type = 265;
		config.name = "Snowball Launcher";
	}

	@Override
	public ArenaItemConfig getConfig() {
		return config;
	}

	@Override
	public void leftClickEvent(ArenaPlayer player) {

	}

	@Override
	public void rightClickEvent(ArenaPlayer player) {
		ProjectileUtil.shootTo(player, Snowball.class, player.forward().mult(100), 10);
	}

	@Override
	public boolean pickupEvent(ArenaPlayer player) {
		return false;
	}

	@Override
	public boolean dropEvent(ArenaPlayer player) {
		return false;
	}

}
