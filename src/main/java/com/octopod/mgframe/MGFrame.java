package com.octopod.mgframe;

import com.octopod.mgframe.abstraction.MinecraftInterface;
import com.octopod.mgframe.world.WorldManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGFrame {

	/**
	 * The current instance of MinecraftInterface
	 */
	private static MinecraftInterface mcif;

	private static WorldManager worldManager;

	public static MinecraftInterface getInterface() {
		return mcif;
	}

	public static void setInterface(MinecraftInterface mcif) {
		MGFrame.mcif = mcif;
	}

	public static void setWorldManager(WorldManager worldManager) {
		MGFrame.worldManager = worldManager;
	}

	public static WorldManager getWorldManager() {
		return worldManager;
	}

    public static List<String> getAllWorlds() {
        return Arrays.asList(MGFramePlugin.getWorldFolder().list(
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

}
