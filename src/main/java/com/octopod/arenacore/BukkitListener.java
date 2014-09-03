package com.octopod.arenacore;

import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.ArenaWeapon;
import com.octopod.arenacore.abstraction.bukkit.BukkitGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class BukkitListener implements Listener {

    @EventHandler
    public void on_click(PlayerInteractEvent event)
    {
        ArenaPlayer gplayer = ArenaCore.getPlayer(event.getPlayer().getUniqueId().toString());
        if(gplayer != null) {
            event.setCancelled(true);

            ArenaWeapon weapon = gplayer.getHandWeapon();

            switch(event.getAction()) {
                case LEFT_CLICK_BLOCK:
                case LEFT_CLICK_AIR:
                    //Secondary weapon fire
                    if(weapon != null) {
                        weapon.secondaryAttack();
                        Bukkit.broadcastMessage("Player " + gplayer.getName() + " has secondary fired with " + weapon.getName());
                    } else {
						Bukkit.broadcastMessage("Player " + gplayer.getName() + " has secondary fired with no weapon.");
					}
                    break;
                case RIGHT_CLICK_BLOCK:
                case RIGHT_CLICK_AIR:
                    if(weapon != null) {
                        weapon.primaryAttack();
                        Bukkit.broadcastMessage("Player " + gplayer.getName() + " has primary fired with " + weapon.getName());
                    } else {
						Bukkit.broadcastMessage("Player " + gplayer.getName() + " has primary fired with no weapon.");
					}
                    break;
            }
        }
    }

    @EventHandler
    public void on_click_entity(PlayerInteractEntityEvent event)
    {
        ArenaPlayer gplayer = ArenaCore.getPlayer(event.getPlayer().getUniqueId().toString());
        if(gplayer != null) {
            //The player has right clicked, but it was on an entity
            event.setCancelled(true);
            ArenaWeapon weapon = gplayer.getHandWeapon();
            if(weapon != null) {
				weapon.primaryAttack();
				Bukkit.broadcastMessage("Player " + gplayer.getName() + " has secondary fired with " + weapon.getName() + " (entity)");
			} else {
				Bukkit.broadcastMessage("Player " + gplayer.getName() + " has secondary fired with no weapon. (entity)");
			}
        }
    }

	@EventHandler
	public void on_command(PlayerCommandPreprocessEvent event)
	{
		Player p = event.getPlayer();
		List<String> parsed = new ArrayList<>(Arrays.asList(event.getMessage().split(" ")));

		String root = parsed.get(0); parsed.remove(0);

		if(root.equalsIgnoreCase("/mgf"))
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
				ArenaWeapon.Script script = ArenaCore.getWeapon(weapID);
				if(script == null)
				{
					p.sendMessage("Weapon with ID " + weapID + " not found");
				} else
				{
					gplayer.giveWeapon(gplayer.getHandSlot(), script);
					p.sendMessage("Weapon ID " + weapID + " given to player " + gplayer.getName());
				}
			}
		}
	}

}
