package com.octopod.mgframe;

import com.octopod.mgframe.abstraction.BukkitInterface;
import com.octopod.mgframe.abstraction.MinecraftInterface;
import com.octopod.mgframe.chatbuilder.ChatUtils;
import com.octopod.mgframe.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGFramePlugin extends JavaPlugin
{
    private static boolean commandHelperEnabled = false;
    private static File worldFolder = null;
    private static LoggerInterface logger = null;

	@Override
	public void onEnable()
	{
		MinecraftInterface mcInterface = new BukkitInterface();
		MGFrame.setInterface(mcInterface);
		MGFrame.setWorldManager(new WorldManager(mcInterface.getWorlds()));

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

        if(commandHelperEnabled) {
            logger.broadcast("&8[&6MGF&8] &aFound CommandHelper!");
        }

        finalCheck();
        logger.broadcast("&8[&6MGF&8] &aMGF v." + this.getDescription().getVersion() + " loaded!");
	}

	@Override
	public void onDisable()
	{

	}

    public static File getWorldFolder() {return worldFolder;}
    public static boolean isCommandHelperEnabled() {return commandHelperEnabled;}

    public void finalCheck() {
        if(logger == null) {
            Bukkit.broadcastMessage("[MGF] The logger wasn't initialized! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
