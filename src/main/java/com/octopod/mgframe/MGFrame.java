package com.octopod.mgframe;

import com.octopod.mgframe.abstraction.MinecraftInterface;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class MGFrame {

	/**
	 * The current instance of MinecraftInterface
	 */
	private static MinecraftInterface mcif;

	public static MinecraftInterface getInterface() {
		return mcif;
	}

	public static void setInterface(MinecraftInterface mcif) {
		MGFrame.mcif = mcif;
	}

}
