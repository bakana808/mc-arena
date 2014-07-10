package com.octopod.mgframe.abstraction;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitInterface implements MinecraftInterface {

	@Override
	public List<String> getWorlds()
	{
		List<World> worlds = Bukkit.getWorlds();
		List<String> names = new ArrayList<>();

		for(World world: worlds) {
			names.add(world.getName());
		}

		return names;
	}

	public void saveWorld(String world)
	{
		Bukkit.getWorld(world).save();
	}

	public void unloadWorld(String world)
	{
		Bukkit.unloadWorld(Bukkit.getWorld(world), false);
	}

	public void createWorld(String world)
	{
		Bukkit.createWorld(new WorldCreator(world));
	}

}
