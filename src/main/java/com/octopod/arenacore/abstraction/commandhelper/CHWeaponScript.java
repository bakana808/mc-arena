package com.octopod.arenacore.abstraction.commandhelper;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.ArenaWeaponConfig;
import com.octopod.arenacore.abstraction.ArenaWeaponScript;
import com.octopod.arenacore.commandhelper.MethodScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CHWeaponScript extends CHScript implements ArenaWeaponScript {

	@Override
	public String[] requiredProcedures() {
		return new String[] {
				"_config",
				"_primaryAttack",
				"_secondaryAttack",
				"_pickupWeapon",
				"_dropWeapon"
		};
	}

	private static List<Construct> NO_ARGS = new ArrayList<>();

	private Procedure dropWeaponProc, pickupWeaponProc, pAttackProc, sAttackProc, configProc;

	private ArenaWeaponConfig config = null;

    public CHWeaponScript(MethodScript script)
    {
		super(script.cloneEnvironment());

        Map<String, Procedure> procs = script.getProcedures();

		if(procs.containsKey("_primaryAttack")) {
			this.pAttackProc = procs.get("_primaryAttack");
		}

		if(procs.containsKey("_secondaryAttack")) {
			this.sAttackProc = procs.get("_secondaryAttack");
		}

		if(procs.containsKey("_config")) {
			this.configProc = procs.get("_config");
		}

		if(procs.containsKey("_dropWeapon")) {
			this.dropWeaponProc = procs.get("_dropWeapon");
		}

		if(procs.containsKey("_pickupWeapon")) {
			this.pickupWeaponProc = procs.get("_pickupWeapon");
		}

		//== LOAD CONFIG ==//

		config = new ArenaWeaponConfig();
		if(configProc != null)
		{
			Target t = Target.UNKNOWN;
			CArray options = configToArray(config, t);
			List<Construct> args = new ArrayList<>();
			args.add(options);

			try {
				config = arrayToConfig((CArray)configProc.execute(args, script.cloneEnvironment(), t), t);
			} catch (ConfigRuntimeException e) {
				printException(e);
			}
		}
    }

	private CArray listToArray(List<Construct> arguments)
	{
		CArray array = new CArray(Target.UNKNOWN);
		for(Construct c: arguments) {
			array.push(c);
		}
		return array;
	}

    @Override
    public void secondaryAttack(final ArenaPlayer player)
	{
        execute(player, sAttackProc, NO_ARGS);
    }

    @Override
    public void primaryAttack(final ArenaPlayer player)
	{
		execute(player, pAttackProc, NO_ARGS);
    }

	@Override
	public void dropWeapon(ArenaPlayer player)
	{
		execute(player, dropWeaponProc, NO_ARGS);
	}

	@Override
	public void pickupWeapon(ArenaPlayer player)
	{
		execute(player, pickupWeaponProc, NO_ARGS);
	}

	@Override
	public ArenaWeaponConfig getConfig() {return config;}

	public static CArray configToArray(ArenaWeaponConfig config, Target t)
	{
		CArray array = new CArray(t);
		CArray description = new CArray(t);
		for(String line: config.description) description.push(new CString(line, t));

		array.set("name", new CString(config.name, t), t);
		array.set("author", new CString(config.author, t), t);
		array.set("description", description, t);
		array.set("item_type", new CInt(config.itemType, t), t);
		array.set("item_data", new CInt(config.itemData, t), t);

		return array;
	}

	public static ArenaWeaponConfig arrayToConfig(CArray array, Target t)
	{
		ArenaWeaponConfig config = new ArenaWeaponConfig();
		config.name = array.get("name", t).val();
		config.author = array.get("author", t).val();

		CArray description = Static.getArray(array.get("description", t), t);
		List<String> descList = new ArrayList<>();
		for(Construct c: description.asList()) descList.add(c.val());

		config.description = descList;

		config.itemType = Static.getInt32(array.get("item_type", t), t);
		config.itemData = Static.getInt32(array.get("item_data", t), t);

		return config;
	}
}
