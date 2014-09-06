package com.octopod.arenacore;

import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.ArenaWeapon;
import com.octopod.arenacore.abstraction.ArenaWeaponScript;
import com.octopod.arenacore.abstraction.bukkit.BukkitGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitListener implements Listener {

	LoggerInterface logger = ArenaCore.getLogger();

	public static ArenaPlayer arenaPlayer(Player player) {
		return ArenaCore.getPlayer(player.getUniqueId().toString());
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
			ArenaWeapon weapon = player.getHandWeapon();
			if(weapon != null)
			{
				logger.broadcast("Player " + player.getName() + " has dropped weapon " + weapon.getName());
				player.setWeapon(player.getHandSlot(), null);
				weapon.scriptDropWeapon();
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
        if(gplayer != null) {
            event.setCancelled(true);

            ArenaWeapon weapon = gplayer.getHandWeapon();

            switch(event.getAction()) {
                case LEFT_CLICK_BLOCK:
                case LEFT_CLICK_AIR:
                    //Secondary weapon fire
                    if(weapon != null) {
                        weapon.scriptSecondaryAttack();
                        logger.broadcast("Player " + gplayer.getName() + " has secondary fired with " + weapon.getName());
                    } else {
						logger.broadcast("Player " + gplayer.getName() + " has secondary fired with no weapon.");
					}
                    break;
                case RIGHT_CLICK_BLOCK:
                case RIGHT_CLICK_AIR:
                    if(weapon != null) {
                        weapon.scriptPrimaryAttack();
						logger.broadcast("Player " + gplayer.getName() + " has primary fired with " + weapon.getName());
                    } else {
						logger.broadcast("Player " + gplayer.getName() + " has primary fired with no weapon.");
					}
                    break;
            }
        }
    }

    @EventHandler
    public void on_click_entity(PlayerInteractEntityEvent event)
    {
        ArenaPlayer gplayer = arenaPlayer(event.getPlayer());
        if(gplayer != null) {
            //The player has right clicked, but it was on an entity
            event.setCancelled(true);
            ArenaWeapon weapon = gplayer.getHandWeapon();
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
		Player p = event.getPlayer();
		List<String> parsed = new ArrayList<>(Arrays.asList(event.getMessage().split(" ")));

		String root = parsed.get(0); parsed.remove(0);

		if(root.equalsIgnoreCase("/arc"))
		{
			event.setCancelled(true);

			if(parsed.size() == 2 && parsed.get(0).equalsIgnoreCase("padd"))
			{
				Player player = Bukkit.getPlayer(parsed.get(1));
				if(player == null) {
					p.sendMessage("Couldn't find player with name '" + parsed.get(1) + "'");
				} else {
					ArenaCore.addPlayer(new BukkitGamePlayer(player));
					p.sendMessage("Successfully added new GamePlayer '" + parsed.get(1) + "'!");
				}
			}

			if(parsed.size() == 1 && parsed.get(0).equalsIgnoreCase("pclear"))
			{
				ArenaCore.clearPlayers();
				p.sendMessage("Successfully cleared all GamePlayers!");
			}

			if(parsed.size() == 3 && parsed.get(0).equalsIgnoreCase("give")) {
				String weapID = parsed.get(1);
				String pname = parsed.get(2);
				ArenaPlayer gplayer = ArenaCore.getPlayerByName(pname);
				if(gplayer == null)
				{
					p.sendMessage("Player with name " + pname + " not found.");
					return;
				}
				ArenaWeaponScript script = ArenaCore.getWeaponScript(weapID);
				if(script == null)
				{
					p.sendMessage("Weapon with ID " + weapID + " not found");
				} else
				{
					ArenaCore.giveWeapon(script, gplayer);
					p.sendMessage("Weapon ID " + weapID + " given to player " + gplayer.getName());
				}
			}

			if(parsed.size() == 3 && parsed.get(0).equalsIgnoreCase("hurt")) {
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
