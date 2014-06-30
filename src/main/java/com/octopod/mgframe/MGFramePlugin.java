package com.octopod.mgframe;

import com.octopod.mgframe.abstraction.BukkitInterface;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGFramePlugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		MGFrame.setInterface(new BukkitInterface());
	}

	@Override
	public void onDisable()
	{

	}
}
