package com.octopod.arenacore;

import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class Configuration {

	public void load() {}

	/**
	 * The list of world names to use in the gamemode
	 */
	private List<String> mapRotation;

	public List<String> getMapRotation() {
		return mapRotation;
	}

}