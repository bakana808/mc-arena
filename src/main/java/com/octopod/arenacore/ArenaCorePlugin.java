package com.octopod.arenacore;

import com.octopod.arenacore.chatbuilder.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class ArenaCorePlugin extends JavaPlugin
{
    private static boolean commandHelperEnabled = false;
    private static File worldFolder = null;
	private static File pluginFolder = null;
    private static LoggerInterface logger = null;

	public static ArenaCorePlugin self;

	@Override
	public void onEnable()
	{
		self = this;
        logger = new LoggerInterface() {
            @Override
            public void broadcast(String message) {
                Bukkit.broadcastMessage(ChatUtils.colorize(message));
            }

            @Override
            public void console(String message) {
                Bukkit.getConsoleSender().sendMessage(ChatUtils.colorize(message));
            }

            @Override
            public void player(UUID lookup, String message) {
                Bukkit.getPlayer(lookup).sendMessage(ChatUtils.colorize(message));
            }
        };

        commandHelperEnabled = Bukkit.getPluginManager().isPluginEnabled("CommandHelper");
        worldFolder = Bukkit.getWorldContainer();
		pluginFolder = this.getDataFolder();

        if(commandHelperEnabled) {
            logger.broadcast("&8[&6ArCore&8] &aFound CommandHelper!");
        }

		Bukkit.getServer().getPluginManager().registerEvents(new BukkitListener(), this);

		ArenaCore.scanWeaponScripts();

        finalCheck();
        logger.broadcast("&8[&6ArCore&8] &aArenaCore v." + this.getDescription().getVersion() + " loaded!");
	}

	@Override
	public void onDisable()
	{

	}

    public static File getWorldFolder() {return worldFolder;}
	public static File getPluginFolder() {return pluginFolder;}

    public static boolean isCommandHelperEnabled() {return commandHelperEnabled;}

	public static LoggerInterface getLoggerIF() {return logger;}

    public void finalCheck() {
        if(logger == null) {
            Bukkit.broadcastMessage("[MGF] The logger wasn't initialized! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
