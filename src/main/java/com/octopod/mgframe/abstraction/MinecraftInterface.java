package com.octopod.mgframe.abstraction;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public interface MinecraftInterface {

	/**
	 * Gets all world names on this server
	 * @return a list containing all world names
	 */
	public List<String> getWorlds();

	public void unloadWorld(String world);

	public void createWorld(String world);

}
