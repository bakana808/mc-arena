package com.octopod.arenacore;

import com.octopod.arenacore.abstraction.ArenaPlayer;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ProjectileMeta
{
	public ArenaPlayer shooter;
	public double direct_damage;
	public double splash_damage;
	public double splash_radius;

	public ProjectileMeta(ArenaPlayer shooter, double damage)
	{
		this(shooter, damage, 0, 0);
	}

	public ProjectileMeta(ArenaPlayer shooter, double direct_damage, double splash_damage, double splash_radius)
	{
		this.shooter = shooter;
		this.direct_damage = direct_damage;
		this.splash_damage = splash_damage;
		this.splash_radius = splash_radius;
	}
}
