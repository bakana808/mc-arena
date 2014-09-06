package com.octopod.arenacore.abstraction;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaClassDefaultScript implements ArenaClassScript {

	ArenaClassConfig config = new ArenaClassConfig();

	@Override
	public ArenaClassConfig getConfig() {
		return config;
	}

}
