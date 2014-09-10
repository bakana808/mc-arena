package com.octopod.arenacore;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.commandhelper.MScript;
import com.octopod.arenacore.items.SnowballGun;
import com.octopod.arenacore.script.ArenaClassDefaultScript;
import com.octopod.arenacore.script.ArenaItemScript;
import com.octopod.arenacore.script.commandhelper.CHItemScript;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaCore {

    private static List<ArenaPlayer> players = new ArrayList<>();

	private static Map<String, ArenaItemScript> itemScripts = null;

    public static void addPlayer(ArenaPlayer player) {
        players.add(player);
		player.setPlayerClass(new ArenaClassDefaultScript());
    }

    public static void removePlayer(ArenaPlayer player) {
        players.remove(player);
    }

    public static ArenaPlayer getPlayer(String UUID) {
        for(ArenaPlayer player: players) {
            if(player.getID().equals(UUID)) {
                return player;
            }
        }
        return null;
    }

	public static ArenaPlayer getPlayerByName(String name) {
		for(ArenaPlayer player: players) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}

	public static void clearPlayers() {
		players = new ArrayList<>();
	}

    public static List<String> getAllWorlds() {
        return Arrays.asList(ArenaCorePlugin.getWorldFolder().list(
                new FilenameFilter()
                {
                    @Override
                    public boolean accept(File current, String name)
                    {
                        return new File(current, name).isDirectory();
                    }
                }
        ));
    }

	public static LoggerInterface getLogger() {
		return ArenaCorePlugin.getLoggerIF();
	}

	public static ArenaItemScript getItemScript(String ID) {
		return itemScripts.get(ID);
	}

	public static void addItemScript(String ID, ArenaItemScript script)
	{
		itemScripts.put(ID, script);
		getLogger().broadcast("&a[LOADED] Item added under ID " + ID);
	}

	public static void giveWeapon(ArenaItemScript script, ArenaPlayer player) {
		giveWeapon(player.getHandSlot(), script, player);
	}

	public static void giveWeapon(int slot, ArenaItemScript script, ArenaPlayer player) {
		player.giveWeapon(slot, script);
	}

	public static void scanWeaponScripts() {
		File weaponFolder = new File(ArenaCorePlugin.getPluginFolder(), "weapons");

		File[] files = weaponFolder.listFiles();

		getLogger().broadcast("&7== SCANNING WEAPON SCRIPTS ==");
		if(files == null) return;

		itemScripts = new HashMap<>();

		for(File file: files) {

			String filename = file.getName();
			if(FilenameUtils.getExtension(filename).equalsIgnoreCase("ms"))
			{
				getLogger().broadcast("&aReading Item script &f" + filename + "&a... (commandhelper)");
				try
				{
					MScript script = new MScript(file);
					script.setDynamicEnv(true);
					CHItemScript weaponScript = new CHItemScript(script);
					Map<String, Procedure> procs = script.getProcedures();
					for(String procName: weaponScript.requiredProcedures()) {
						if(!procs.containsKey(procName)) {
							getLogger().broadcast("&e - WARNING: Script is missing " + procName + " procedure!");
						}
					}
					//Weapon ID is the file's base name, whatever that is
					String weapID = FilenameUtils.getBaseName(filename);
					addItemScript(weapID, weaponScript);
				} catch (IOException e) {
					getLogger().broadcast("&c - ERROR: Unable to read this file!");
				} catch (ConfigCompileException e) {
					getLogger().broadcast("&c - ERROR: Unable to compile this file!");
					getLogger().broadcast("&c   - " + e.getMessage());
				}
			}
		}

		addItemScript("snowball", new SnowballGun());

	}

}
