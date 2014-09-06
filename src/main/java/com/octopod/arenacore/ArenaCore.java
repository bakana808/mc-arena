package com.octopod.arenacore;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.octopod.arenacore.abstraction.ArenaClassDefaultScript;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.ArenaWeaponScript;
import com.octopod.arenacore.abstraction.commandhelper.CHWeaponScript;
import com.octopod.arenacore.commandhelper.MethodScript;
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

	private static Map<String, ArenaWeaponScript> weaponScripts = null;

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

	public static ArenaWeaponScript getWeaponScript(String ID) {
		return weaponScripts.get(ID);
	}

	public static void giveWeapon(ArenaWeaponScript script, ArenaPlayer player) {
		giveWeapon(player.getHandSlot(), script, player);
	}

	public static void giveWeapon(int slot, ArenaWeaponScript script, ArenaPlayer player) {
		player.giveWeapon(slot, script);
	}

	public static void scanWeaponScripts() {
		File weaponFolder = new File(ArenaCorePlugin.getPluginFolder(), "weapons");

		File[] files = weaponFolder.listFiles();

		getLogger().broadcast("&7== SCANNING WEAPON SCRIPTS ==");
		if(files == null) return;

		weaponScripts = new HashMap<>();

		for(File file: files) {

			String filename = file.getName();
			if(FilenameUtils.getExtension(filename).equalsIgnoreCase("ms"))
			{
				getLogger().broadcast("&aReading weapon script &f" + filename + "&a... (commandhelper)");
				try
				{
					MethodScript script = new MethodScript(file);
					CHWeaponScript weaponScript = new CHWeaponScript(script);
					Map<String, Procedure> procs = script.getProcedures();
					for(String procName: weaponScript.requiredProcedures()) {
						if(!procs.containsKey(procName)) {
							getLogger().broadcast("&e - WARNING: Script is missing " + procName + " procedure!");
						}
					}
					//Weapon ID is the file's base name, whatever that is
					String weapID = FilenameUtils.getBaseName(filename);
					weaponScripts.put(weapID, weaponScript);
					getLogger().broadcast("&a - Weapon added under ID " + weapID);
				} catch (IOException e) {
					getLogger().broadcast("&c - ERROR: Unable to read this file!");
				} catch (ConfigCompileException e) {
					getLogger().broadcast("&c - ERROR: Unable to compile this file!");
					getLogger().broadcast("&c   - " + e.getMessage());
				}
			}
		}

	}

}
