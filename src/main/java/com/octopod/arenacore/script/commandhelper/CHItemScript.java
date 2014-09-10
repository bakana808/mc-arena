package com.octopod.arenacore.script.commandhelper;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.octopod.arenacore.abstraction.ArenaItemConfig;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.commandhelper.MScript;
import com.octopod.arenacore.script.ArenaItemScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CHItemScript extends CommandHelperScript implements ArenaItemScript
{
	@Override
	public String[] requiredProcedures() {
		return new String[] {
				"_right_click",
				"_left_click",
				"_pickup",
				"_drop"
		};
	}

	private ArenaItemConfig config;
	private Map<String, Procedure> procedures;
	private Target t;

    public CHItemScript(MScript script)
    {
		t = script.getTarget();
		config = new ArenaItemConfig();
		script.setVariable("@config", configToArray(config, t));
		script.setVariable("@static", new CArray(t));

		procedures = script.getProcedures();

		Construct cConfig = script.getVariable("@config");
		if(cConfig instanceof CArray) config = arrayToConfig((CArray)cConfig, t);

		setOriginalEnvironment(script.cloneEnvironment());
    }

    @Override
    public void leftClickEvent(final ArenaPlayer player)
	{
		if(procedures.containsKey("_left_click"))
		{
        	executeProcedure(player, procedures.get("_left_click"), NO_ARGS);
		}
    }

    @Override
    public void rightClickEvent(final ArenaPlayer player)
	{
		if(procedures.containsKey("_right_click"))
		{
			executeProcedure(player, procedures.get("_right_click"), NO_ARGS);
		}
    }

	@Override
	public boolean dropEvent(ArenaPlayer player)
	{
		if(procedures.containsKey("_drop"))
		{
			CArray event = new CArray(t);
			event.set("cancelled", CBoolean.FALSE, t);

			List<Construct> args = new ArrayList<>();
			args.add(event);

			Construct ret = executeProcedure(player, procedures.get("_drop"), args);

			if(!(ret instanceof CArray)) return false;
			event = (CArray)ret;
			return Static.getBoolean(event.get("cancelled", t));
		}
		return false;
	}

	@Override
	public boolean pickupEvent(ArenaPlayer player)
	{
		if(procedures.containsKey("_pickup"))
		{
			CArray event = new CArray(t);
			event.set("cancelled", CBoolean.FALSE, t);

			List<Construct> args = new ArrayList<>();
			args.add(event);

			Construct ret = executeProcedure(player, procedures.get("_pickup"), args);

			if(!(ret instanceof CArray)) return false;
			event = (CArray)ret;
			return Static.getBoolean(event.get("cancelled", t));
		}
		return false;
	}

	@Override
	public ArenaItemConfig getConfig() {
		return config;
	}

	public static CArray configToArray(ArenaItemConfig config, Target t)
	{
		return getFields(config, t);
	}

	public static ArenaItemConfig arrayToConfig(CArray array, Target t)
	{
		ArenaItemConfig config = new ArenaItemConfig();

		config.name = 			array.get("name", t).val();
		config.author = 		array.get("author", t).val();
		config.item_type = 		Static.getInt32(array.get("item_type", t), t);
		config.item_data = 		Static.getInt32(array.get("item_data", t), t);
		config.can_drop =		Static.getBoolean(array.get("can_drop", t));

		return config;
	}
}
