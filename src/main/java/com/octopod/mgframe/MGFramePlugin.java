package com.octopod.mgframe;

import com.octopod.mgframe.abstraction.BukkitInterface;
import com.octopod.mgframe.abstraction.MinecraftInterface;
import com.octopod.mgframe.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGFramePlugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		MinecraftInterface mcInterface = new BukkitInterface();
		MGFrame.setInterface(mcInterface);
		MGFrame.setWorldManager(new WorldManager(mcInterface.getWorlds()));
	}

	@Override
	public void onDisable()
	{

	}
}
