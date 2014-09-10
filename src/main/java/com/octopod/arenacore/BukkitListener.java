package com.octopod.arenacore;

import com.octopod.arenacore.abstraction.ArenaItem;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.bukkit.BukkitArenaPlayer;
import com.octopod.arenacore.chatbuilder.ChatUtils;
import com.octopod.arenacore.script.ArenaItemScript;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitListener implements Listener {

	LoggerInterface logger = ArenaCore.getLogger();

	public static ArenaPlayer arenaPlayer(Entity entity) {
		if(entity instanceof Player)
			return ArenaCore.getPlayer(entity.getUniqueId().toString());
			return null;
	}

	@EventHandler
	public void projectile_hit(ProjectileHitEvent event)
	{
		ProjectileMeta meta = ProjectileUtil.pop(event.getEntity());
	}

	@EventHandler
	public void entity_damage_entity(EntityDamageByEntityEvent event)
	{
		Entity damager = event.getDamager();
		if(damager instanceof Projectile)
		{
			ProjectileMeta meta = ProjectileUtil.pop((Projectile) damager);
			ArenaPlayer victim = arenaPlayer(event.getEntity());
			if(meta != null)
				{
				if(victim != null)
				{
					//An ArenaPlayer; use the hurt method;
					event.setCancelled(true);
					victim.hurt((int)meta.direct_damage);
					Bukkit.broadcastMessage("Player " + victim.getName() + " hurt by " + meta.shooter.getName() + " with " + (int)meta.direct_damage + " damage (direct)");
				} else {
					//Just a normal player/entity; damage normally
					event.setDamage(meta.direct_damage);
				}
			}
		}
	}

	@EventHandler
	public void food_change(FoodLevelChangeEvent event) {
		if(event.getEntityType() == EntityType.PLAYER) {
			ArenaPlayer player = arenaPlayer((Player)event.getEntity());
			if(player != null) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void toggle_fly(PlayerToggleFlightEvent event) {
		ArenaPlayer player = arenaPlayer(event.getPlayer());
		if(player != null) {
			logger.broadcast("Player " + player.getName() + " has toggled fly.");
			//run Class script for fly toggle
		}
	}

	@EventHandler
	public void entity_damage(EntityDamageEvent event) {
		if(event.getEntityType() == EntityType.PLAYER) {
			ArenaPlayer player = arenaPlayer((Player)event.getEntity());
			if(player != null) {
				event.setCancelled(true);
				player.hurt((int)event.getDamage());
			}
		}
	}

	@EventHandler
	public void drop_item(PlayerDropItemEvent event)
	{
		ArenaPlayer player = arenaPlayer(event.getPlayer());
		if(player != null)
		{
			ArenaItem weapon = player.getHandWeapon();
			if(weapon != null)
			{
				logger.broadcast("Player " + player.getName() + " has dropped weapon " + weapon.getName());
				player.setWeapon(player.getHandSlot(), null);
				event.setCancelled(weapon.scriptDropWeapon());
			}
		}
	}

	@EventHandler
	public void player_leave(PlayerQuitEvent event)
	{
		ArenaPlayer aplayer = arenaPlayer(event.getPlayer());
		if(aplayer != null) {
			ArenaCore.removePlayer(aplayer);
		}
	}

    @EventHandler
    public void on_click(PlayerInteractEvent event)
    {
        ArenaPlayer gplayer = arenaPlayer(event.getPlayer());
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE && gplayer != null) {
            event.setCancelled(true);

            ArenaItem weapon = gplayer.getHandWeapon();

            switch(event.getAction()) {
                case LEFT_CLICK_BLOCK:
                case LEFT_CLICK_AIR:
                    //Secondary weapon fire
                    if(weapon != null) {
                        weapon.scriptSecondaryAttack();
                    } else {
						//Run left click script for player class
					}
                    break;
                case RIGHT_CLICK_BLOCK:
                case RIGHT_CLICK_AIR:
                    if(weapon != null) {
                        weapon.scriptPrimaryAttack();
                    } else {
						//Run right click script for player class
					}
                    break;
            }
        }
    }

    @EventHandler
    public void on_click_entity(PlayerInteractEntityEvent event)
    {
        ArenaPlayer gplayer = arenaPlayer(event.getPlayer());
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE && gplayer != null) {
            //The player has right clicked, but it was on an entity
            event.setCancelled(true);
            ArenaItem weapon = gplayer.getHandWeapon();
            if(weapon != null) {
				weapon.scriptPrimaryAttack();
			} else {
				logger.broadcast("Player " + gplayer.getName() + " has secondary fired with no weapon. (entity)");
			}
        }
    }

	@EventHandler
	public void on_command(PlayerCommandPreprocessEvent event)
	{
		final Player p = event.getPlayer();
		List<String> parsed = new ArrayList<>(Arrays.asList(event.getMessage().split(" ")));

		String root = parsed.get(0); parsed.remove(0);

		if(root.equalsIgnoreCase("/acore"))
		{
			event.setCancelled(true);

			if(parsed.size() >= 1 && parsed.get(0).equalsIgnoreCase("debug"))
			{
				if(parsed.size() >= 2)
				{
					//Create Player Command
					if(parsed.size() == 2 && parsed.get(1).equalsIgnoreCase("holotest"))
					{
						Hologram hologram = new Hologram(p.getLocation(), 0.25, logo());
						p.sendMessage("Hologram Created with ID " + hologram.getId());
					}

					if(parsed.size() == 2 && parsed.get(1).equalsIgnoreCase("bartest"))
					{
						final MessageBar bar = MessageBar.addMessageBar(p, " ");
						p.sendMessage("Bar Created");
						Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaCorePlugin.self, new Thread() {
							@Override
							public void run() {
								bar.setHealth(bar.getHealth() - 1);
								bar.setMessage("==[ Test Bar | Count " + (bar.getHealth() - 1) + " ]==");
								MessageBar.sendPacket(p, bar.getMetaPacket());
								MessageBar.sendPacket(p, bar.getTeleportPacket(p.getLocation().add(0, -300, 0)));
							}
						}, 20L, 20L);
					}

					if(parsed.size() == 3 && parsed.get(1).equalsIgnoreCase("holokill"))
					{
						int id = Integer.parseInt(parsed.get(2));
						Hologram.kill(id);
						p.sendMessage("Hologram Killed with ID " + id);
					}

					//Create Player Command
					if(parsed.size() == 3 && parsed.get(1).equalsIgnoreCase("createplayer"))
					{
						Player player = Bukkit.getPlayer(parsed.get(2));
						if(player == null) {
							p.sendMessage("Couldn't find player with name '" + parsed.get(2) + "'");
						} else {
							ArenaCore.addPlayer(new BukkitArenaPlayer(player));
							p.sendMessage("Successfully created new GamePlayer '" + parsed.get(2) + "'!");
						}
						return;
					}

					//Clear Players Command
					if(parsed.size() == 2 && parsed.get(1).equalsIgnoreCase("clearplayers"))
					{
						ArenaCore.clearPlayers();
						p.sendMessage("Successfully cleared all GamePlayers!");
						return;
					}

					//Give Item Command
					if(parsed.size() == 4 && parsed.get(1).equalsIgnoreCase("giveitem"))
					{
						String pname = parsed.get(2);
						String itemID = parsed.get(3);
						ArenaPlayer gplayer = ArenaCore.getPlayerByName(pname);
						if(gplayer == null)
						{
							p.sendMessage("Player with name " + pname + " not found.");
							return;
						}
						ArenaItemScript script = ArenaCore.getItemScript(itemID);
						if(script == null)
						{
							p.sendMessage("Item with ID " + itemID + " not found");
						} else
						{
							ArenaCore.giveWeapon(script, gplayer);
							p.sendMessage("Item ID " + itemID + " given to player " + gplayer.getName());
						}
						return;
					}

					if(parsed.size() == 4 && parsed.get(0).equalsIgnoreCase("hurt")) {
						String pname = parsed.get(1);
						ArenaPlayer gplayer = ArenaCore.getPlayerByName(pname);
						if(gplayer == null)
						{
							p.sendMessage("Player with name " + pname + " not found.");
							return;
						}
						int amount;
						try {
							amount = Integer.parseInt(parsed.get(2));
						} catch (NumberFormatException e) {
							p.sendMessage("Amount of damage is not a number");
							return;
						}
						gplayer.hurt(amount);
						p.sendMessage("Player " + pname + " hurt with " + amount + " damage");
					}

				}

			}


		}
	}

	public String[] logo()
	{
		return new String[]
		{
				ChatUtils.colorize("&0▂▂▂▂▂▂▂▂▂▂▂▂"),
				ChatUtils.colorize("&0▂▂▂▂▂▂▂▂▂▂▂▂▂▂"),
				ChatUtils.colorize("&0▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂"),
				ChatUtils.colorize("&0▂▂▂&c██&0▂▂▂▂▂▂&c██&0▂▂▂"),
				ChatUtils.colorize("&0▂▂▂&c██&0▂▂▂▂▂▂&c██&0▂▂▂"),
				ChatUtils.colorize("&0▂▂▂▂▂&c██&0▂▂&c██&0▂▂▂▂▂"),
				ChatUtils.colorize("&0▂▂&f███&c██&0▂▂&c██&f███&0▂▂"),
				ChatUtils.colorize("&0▂&f██&0▂&f██&0▂&c██&0▂&f██&0▂&f██&0▂"),
				ChatUtils.colorize("&0▂&f██&0▂&f██&0▂&c██&0▂&f██&0▂&f██&0▂"),
				ChatUtils.colorize("&0▂&f██&0▂&f██&c█&0▂▂&c█&f██&0▂&f██&0▂"),
				ChatUtils.colorize("&0▂▂▂▂▂&c██&0▂▂&c██&0▂▂▂▂▂"),
				ChatUtils.colorize("&0▂▂▂&c██&0▂▂▂▂▂▂&c██&0▂▂▂"),
				ChatUtils.colorize("&0▂▂▂&c██&0▂▂▂▂▂▂&c██&0▂▂▂"),
				ChatUtils.colorize("&0▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂"),
				ChatUtils.colorize("&0▂▂▂▂▂▂▂▂▂▂▂▂▂▂"),
				ChatUtils.colorize("&0▂▂▂▂▂▂▂▂▂▂▂▂"),
		};
	}

}
