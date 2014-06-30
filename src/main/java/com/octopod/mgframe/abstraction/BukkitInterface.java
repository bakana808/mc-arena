package com.octopod.mgframe.abstraction;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitInterface implements MinecraftInterface {

	@Override
	public String[] getWorlds()
	{
		List<World> worlds = Bukkit.getWorlds();
		String[] names = new String[worlds.size()];

		for(int i = 0; i < worlds.size(); i++)
			names[i] = worlds.get(i).getName();

		return names;
	}

}
