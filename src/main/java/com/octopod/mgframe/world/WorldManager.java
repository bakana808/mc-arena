package com.octopod.mgframe.world;

import com.octopod.mgframe.MGFrame;
import com.octopod.mgframe.abstraction.MinecraftInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class WorldManager {

	public List<String> allWorlds = new ArrayList<>();
	public List<String> loadedWorlds = new ArrayList<>();

	public MinecraftInterface mcInterface = MGFrame.getInterface();

	public WorldManager(List<String> worlds) {
		allWorlds = new ArrayList<>(worlds);
		loadedWorlds = new ArrayList<>(worlds);
	}

	/**
	 * Unloads all worlds except those specified in the whitelist
	 * @param whitelist the list of worlds to keep loaded
	 */
	public void unloadAllWorlds(List<String> whitelist) {
		for(String world: loadedWorlds) {
			if(!whitelist.contains(world)) {
				mcInterface.unloadWorld(world);
			}
		}
	}

	/**
	 * Loads the worlds specified in the list
	 * @param worlds the list of worlds to load
	 */
	public void loadWorlds(List<String> worlds) {
		for(String world: worlds) {
			if(allWorlds.contains(world)) {
				mcInterface.createWorld(world);
			}
		}
	}

}
