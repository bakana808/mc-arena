package com.octopod.arenacore;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.ArenaWeapon;
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

	private static Map<String, ArenaWeapon.Script> weapons = null;

    public static void addPlayer(ArenaPlayer player) {
        players.add(player);
    }

    public static void removePlayer(ArenaPlayer player) {
        players.remove(player);
    }

    public static ArenaPlayer getPlayer(String ID) {
        for(ArenaPlayer player: players) {
            if(player.getID().equals(ID)) {
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

	public static ArenaWeapon.Script getWeapon(String ID) {
		return weapons.get(ID);
	}

	public static void scanWeapons() {
		File weaponFolder = new File(ArenaCorePlugin.getPluginFolder(), "weapons");

		File[] files = weaponFolder.listFiles();

		getLogger().broadcast("&7== SCANNING WEAPON SCRIPTS ==");
		if(files == null) return;

		weapons = new HashMap<>();

		for(File file: files) {

			String filename = file.getName();
			if(FilenameUtils.getExtension(filename).equalsIgnoreCase("ms"))
			{
				getLogger().broadcast("&aReading weapon script &f" + filename + "&a... (commandhelper)");
				try
				{
					MethodScript script = new MethodScript(file);
					Map<String, Procedure> procs = script.getProcedures();
					if(!procs.containsKey("_primaryAttack")) {
						getLogger().broadcast("&e - WARNING: Script is missing _primaryAttack() procedure!");
					}
					if(!procs.containsKey("_secondaryAttack")) {
						getLogger().broadcast("&e - WARNING: Script is missing _secondaryAttack() procedure!");
					}
					if(!procs.containsKey("_config")) {
						getLogger().broadcast("&e - WARNING: Script is missing _config() procedure!");
					}
					//Weapon ID is the file's base name, whatever that is
					String weapID = FilenameUtils.getBaseName(filename);
					weapons.put(weapID, new CHWeaponScript(script));
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
