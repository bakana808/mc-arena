package com.octopod.mgframe;

import com.octopod.mgframe.abstraction.BukkitInterface;
import com.octopod.mgframe.abstraction.MinecraftInterface;
import com.octopod.mgframe.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGFramePlugin extends JavaPlugin
{
    private static boolean commandHelperEnabled = false;
    private static File worldFolder = null;

	@Override
	public void onEnable()
	{
		MinecraftInterface mcInterface = new BukkitInterface();
		MGFrame.setInterface(mcInterface);
		MGFrame.setWorldManager(new WorldManager(mcInterface.getWorlds()));

        commandHelperEnabled = Bukkit.getPluginManager().isPluginEnabled("CommandHelper");
        worldFolder = Bukkit.getWorldContainer();
	}

	@Override
	public void onDisable()
	{

	}

    public static File getWorldFolder() {return worldFolder;}
    public static boolean isCommandHelperEnabled() {return commandHelperEnabled;}
}
