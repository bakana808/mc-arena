package com.octopod.arenacore.abstraction.commandhelper;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.octopod.arenacore.ArenaCore;
import com.octopod.arenacore.LoggerInterface;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.ArenaWeapon;
import com.octopod.arenacore.abstraction.bukkit.BukkitGamePlayer;
import com.octopod.arenacore.commandhelper.MethodScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class CHWeaponScript implements ArenaWeapon.Script{

	private MethodScript script;
	private Procedure pAttackProc, sAttackProc, configProc;
	//private MethodScript configScript = null;

	private ArenaWeapon.Config config = null;

    public CHWeaponScript(MethodScript script)
    {
		this.script = script;
        Map<String, Procedure> procs = script.getProcedures();
		if(procs.containsKey("_primaryAttack")) {
			pAttackProc = procs.get("_primaryAttack");
		}

		if(procs.containsKey("_secondaryAttack")) {
			sAttackProc = procs.get("_secondaryAttack");
		}

		if(procs.containsKey("_config")) {
			//configScript = new MethodScript("_config(@options)").include("_config", procs.get("_config"));
			configProc = procs.get("_config");
		}

		config = getConfig(new ArenaWeapon.Config());
    }

    @Override
    public void secondaryAttack(ArenaPlayer player)
	{
        if(sAttackProc != null && player instanceof BukkitGamePlayer)
		{
			final List<Construct> args = new ArrayList<>();
			final Environment e = script.cloneEnvironment();
			args.add(new CString(player.getName(), Target.UNKNOWN));

			new Thread() {
				@Override
				public void run() {
					try {
						sAttackProc.execute(args, e, Target.UNKNOWN);
					} catch (ConfigRuntimeException e) {
						printException(e);
					}
				}
			}.start();
        }
    }

    @Override
    public void primaryAttack(ArenaPlayer player)
	{
		if(pAttackProc != null && player instanceof BukkitGamePlayer)
		{
			final List<Construct> args = new ArrayList<>();
			final Environment e = script.cloneEnvironment();
			args.add(new CString(player.getName(), Target.UNKNOWN));

			new Thread() {
				@Override
				public void run() {
					try {
						pAttackProc.execute(args, e, Target.UNKNOWN);
					} catch (ConfigRuntimeException e) {
						printException(e);
					}
				}
			}.start();
		}
    }

	@Override
	public void dropWeapon(ArenaPlayer player)
	{

	}

	@Override
	public ArenaWeapon.Config getConfig(ArenaWeapon.Config defaults)
	{
		if(config != null) return config;
		if(configProc != null)
		{
			Target t = Target.UNKNOWN;
			CArray options = configToArray(defaults, t);
			List<Construct> args = new ArrayList<>();
			args.add(options);

			try {
				return arrayToConfig((CArray)configProc.execute(args, script.cloneEnvironment(), t), t);
			} catch (ConfigRuntimeException e) {
				printException(e);
			}

			//Get the changes in the @options array, after it may or may not be changed during execution
			//return arrayToConfig((CArray)configScript.getVariable("@options"), t);
		}
		return defaults;
	}

	public static void printException(ConfigRuntimeException e) {
		LoggerInterface logger = ArenaCore.getLogger();
		logger.broadcast("&8[&6ACore&8] &cException running CH script " + e.getSimpleFile() + "! &e(L" + e.getLineNum() + ")");
		logger.broadcast("&8[&6ACore&8] &c" + e.getExceptionType().getName() + ": &7" + e.getMessage());
	}

	public static CArray configToArray(ArenaWeapon.Config config, Target t)
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

	public static ArenaWeapon.Config arrayToConfig(CArray array, Target t)
	{
		ArenaWeapon.Config config = new ArenaWeapon.Config();
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
