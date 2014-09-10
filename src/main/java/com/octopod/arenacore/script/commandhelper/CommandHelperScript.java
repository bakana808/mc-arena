package com.octopod.arenacore.script.commandhelper;

import com.laytonsmith.abstraction.bukkit.BukkitMCPlayer;
import com.laytonsmith.core.ParseTree;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Script;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.octopod.arenacore.ArenaCore;
import com.octopod.arenacore.LoggerInterface;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.bukkit.BukkitArenaPlayer;
import com.octopod.arenacore.commandhelper.MScriptUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class CommandHelperScript {

	protected static List<Construct> NO_ARGS = new ArrayList<>();

	/**
	 * Returns the required procedures to overwrite for this script.
	 * @return
	 */
	public abstract String[] requiredProcedures();

	/**
	 * Player-Specific Environments for each script
	 */
	private Map<ArenaPlayer, Environment> environments = new HashMap<>();

	/**
	 * The original Environment to clone for players
	 */
	private Environment originalEnv = null;

	/**
	 * Gets a player-specific environment for their scripts to run on.
	 * Will clone the original environment if one isn't already made.
	 * @param player
	 * @return
	 */
	public Environment getEnvironment(ArenaPlayer player)
	{
		if(!environments.containsKey(player))
		{
			Environment clonedEnv = MScriptUtils.cloneEnvironment(originalEnv);
			if(clonedEnv != null)
			{
				clonedEnv.getEnv(CommandHelperEnvironment.class).SetCommandSender(new BukkitMCPlayer((Player)player.getHandle()));
				setEnvironment(player, clonedEnv);
			}
		}
		return environments.get(player);
	}

	/**
	 * Sets the original environment.
	 * @param env
	 */
	public void setOriginalEnvironment(Environment env) {
		originalEnv = env;
	}

	/**
	 * Sets a player's specific environment for this script.
	 * @param player
	 * @param env
	 */
	public void setEnvironment(ArenaPlayer player, Environment env) {
		environments.put(player, env);
	}

	/**
	 * Sets variables in the same sense as a procedure does:
	 * Sets all the provided arguments to a variable <code>@arguments</code> and
	 * overwrites the original variables one-by-one with the provided constructs.
	 * @param env
	 * @param originalVars
	 * @param args
	 * @return
	 */
	protected Environment prepareProcedureVariables(Environment env, Map<String, Construct> originalVars, List<Construct> args)
	{
		IVariableList envVars = env.getEnv(GlobalEnv.class).GetVarList();

		int i = 0;
		for(String varName: originalVars.keySet())
		{
			if(args.size() - 1 < i) {
				//Use original constructs
				envVars.set(new IVariable(varName, originalVars.get(varName), Target.UNKNOWN));
			} else {
				//Use provided constructs
				envVars.set(new IVariable(varName, args.get(i), Target.UNKNOWN));
			}
			i++;
		}

		//Set @arguments variable
		CArray argArray = new CArray(Target.UNKNOWN);
		for(Construct c: args) argArray.push(c);
		envVars.set(new IVariable("@arguments", argArray, Target.UNKNOWN));

		env.getEnv(GlobalEnv.class).SetVarList(envVars);
		return env;
	}

	/**
	 * Executes a procedure as a player with the list of arguments.
	 * The only intended difference between the Procedure itself executing it and this method is that
	 * it doesn't clear the Environment's original variables.
	 * @param player
	 * @param procedure
	 * @param arguments
	 */
	protected Construct executeProcedure(ArenaPlayer player, Procedure procedure, List<Construct> arguments)
	{
		if(procedure != null && player instanceof BukkitArenaPlayer)
		{
			try
			{
				ParseTree tree = 						MScriptUtils.getProcedureTree(procedure);
				Map<String, Construct> originalVars = 	MScriptUtils.getProcedureVars(procedure);
				Environment env = prepareProcedureVariables(getEnvironment(player), originalVars, arguments);

				return Script.GenerateScript(tree, env.getEnv(GlobalEnv.class).GetLabel()).eval(tree, env);
			}
			catch (ConfigRuntimeException e)
			{
				printException(e);
			}
		}
		return CNull.NULL;
	}

	/**
	 * Prints a CH exception
	 * @param e
	 */
	public static void printException(ConfigRuntimeException e) {
		LoggerInterface logger = ArenaCore.getLogger();
		logger.broadcast("&8[&6ACore&8] &cException in CommandHelper! &e" + e.getSimpleFile() + ":&a" + e.getLineNum());
		logger.broadcast("&8[&6ACore&8] &c" + e.getExceptionType().getName() + ": &6" + e.getMessage());
	}

	/**
	 * Converts a CArray to a list of strings
	 * @param array
	 * @return
	 */
	protected static List<String> getList(CArray array)
	{
		List<String> list = new ArrayList<>();
		for(Construct c: array.asList()) list.add(c.val());
		return list;
	}

	/**
	 * Gets all the fields of an object and turns it into an associative array.
	 * @param obj
	 * @param t
	 * @return
	 */
	protected static CArray getFields(Object obj, Target t)
	{
		Field[] fields = obj.getClass().getDeclaredFields();
		CArray array = new CArray(t);
		for(Field f: fields) {
			try {
				Object o = f.get(obj);
				if(o instanceof Integer) {
					array.set(f.getName(), new CInt((Integer)o, t), t);
				} else {
					array.set(f.getName(), Static.getMSObject(f.get(obj), t), t);
				}
			} catch (IllegalAccessException e) {}
		}
		return array;
	}

}
