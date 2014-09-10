package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.script.ArenaClassScript;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaClass {

	ArenaPlayer owner;

	ArenaClassScript script;

	public ArenaClass(ArenaPlayer owner, ArenaClassScript script) {
		this.owner = owner;
		this.script = script;
	}

	public ArenaClassConfig getConfig() {
		return script.getConfig();
	}

}