package com.octopod.arenacore.abstraction;

import com.octopod.arenacore.ArenaConfiguration;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaClassConfig implements ArenaConfiguration
{
	public String name = "Default";
	public String author = "";

	public int health_max = 20;
	public int health = 20;

	public float walk_speed = 0.3f;

	public int food = 20;
	public boolean hunger = false;

	public boolean flying = true;
}
