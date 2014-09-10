package com.octopod.arenacore.script;

import com.octopod.arenacore.abstraction.ArenaClassConfig;

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
