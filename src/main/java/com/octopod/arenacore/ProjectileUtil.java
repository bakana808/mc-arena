package com.octopod.arenacore;

import com.octopod.arenacore.abstraction.ArenaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ProjectileUtil
{
	private static Map<Projectile, ProjectileMeta> projectiles = new HashMap<>();

	public static ProjectileMeta pop(Projectile projectile)
	{
		return projectiles.remove(projectile);
	}
	public static void shootTo(ArenaPlayer player, Class<? extends Projectile> type, Vector vel, int damage)
	{
		Projectile projectile = ((Player)player.getHandle()).launchProjectile(type);
		projectile.setVelocity(new org.bukkit.util.Vector(vel.X, vel.Y, vel.Z));
		projectiles.put(projectile, new ProjectileMeta(player, damage));
	}


}
