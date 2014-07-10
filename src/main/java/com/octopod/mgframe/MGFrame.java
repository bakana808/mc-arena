package com.octopod.mgframe;

import com.octopod.mgframe.abstraction.MinecraftInterface;
import com.octopod.mgframe.world.WorldManager;

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

}
