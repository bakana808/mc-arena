package com.octopod.arenacore.script.commandhelper;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.octopod.arenacore.abstraction.ArenaClassConfig;
import com.octopod.arenacore.script.ArenaClassScript;
import com.octopod.arenacore.commandhelper.MScript;

import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CHClassScript extends CommandHelperScript implements ArenaClassScript {

	@Override
	public String[] requiredProcedures() {
		return new String[] {
				"_config",
				"_flyOn",
				"_flyOff",
				"_primaryAttack",
				"_secondaryAttack"
		};
	}

	private ArenaClassConfig config;

	public CHClassScript(MScript script)
	{
		Target t = Target.UNKNOWN;

		config = new ArenaClassConfig();
		script.setVariable("@config", configToArray(config, t));

		Map<String, Procedure> procedures = script.getProcedures();

		Construct cConfig = script.getVariable("@config");

		if(cConfig instanceof CArray) config = arrayToConfig((CArray)cConfig, t);

		setOriginalEnvironment(script.cloneEnvironment());
	}

	@Override
	public ArenaClassConfig getConfig() {
		return config;
	}

	public static CArray configToArray(ArenaClassConfig config, Target t)
	{
		return getFields(config, t);
	}

	public static ArenaClassConfig arrayToConfig(CArray array, Target t)
	{
		ArenaClassConfig config = new ArenaClassConfig();

		config.name = 			array.get("name", t).val();
		config.author = 		array.get("author", t).val();
		config.health_max = 	Static.getInt32(array.get("health_max", t), t);
		config.health =			Static.getInt32(array.get("health", t), t);
		config.food =			Static.getInt32(array.get("food", t), t);
		config.hunger =			Static.getBoolean(array.get("hunger", t));
		config.flying = 		Static.getBoolean(array.get("flying", t));

		return config;
	}
}
