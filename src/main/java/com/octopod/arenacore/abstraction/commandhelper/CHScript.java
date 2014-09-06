package com.octopod.arenacore.abstraction.commandhelper;

import com.laytonsmith.abstraction.bukkit.BukkitMCPlayer;
import com.laytonsmith.core.ParseTree;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Script;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.octopod.arenacore.ArenaCore;
import com.octopod.arenacore.LoggerInterface;
import com.octopod.arenacore.abstraction.ArenaPlayer;
import com.octopod.arenacore.abstraction.bukkit.BukkitGamePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public abstract class CHScript {

	public abstract String[] requiredProcedures();

	private Map<ArenaPlayer, Environment> environments = new HashMap<>();
	private Environment originalEnv;

	/**
	 * Gets a player-specific environment for their scripts to run on.
	 * Will clone the original environment if one isn't already made.
	 * @param player
	 * @return
	 */
	public Environment getEnvironment(ArenaPlayer player) {
		if(!environments.containsKey(player)) {
			try {
				Environment clonedEnv = originalEnv.clone();
				clonedEnv.getEnv(CommandHelperEnvironment.class).SetCommandSender(new BukkitMCPlayer((Player)player.getHandle()));
				environments.put(player, clonedEnv);
			} catch (CloneNotSupportedException e) {}
		}
		return environments.get(player);
	}

	protected CHScript(Environment originalEnv) {
		this.originalEnv = originalEnv;
	}

	protected ParseTree extractTree(Procedure procedure) {
		try {
			Field field = procedure.getClass().getDeclaredField("tree");
			field.setAccessible(true);
			return (ParseTree)field.get(procedure);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Construct> extractOriginalArgs(Procedure procedure) {
		try {
			Field field = procedure.getClass().getDeclaredField("tree");
			field.setAccessible(true);
			return (Map<String, Construct>)field.get(procedure);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected Environment iterateAndOverwrite(Environment env, Map<String, Construct> originalVars, List<Construct> args)
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

		CArray argArray = new CArray(Target.UNKNOWN);
		for(Construct c: args) argArray.push(c);
		envVars.set(new IVariable("@arguments", argArray, Target.UNKNOWN));

		env.getEnv(GlobalEnv.class).SetVarList(envVars);
		return env;
	}

	protected void execute(ArenaPlayer player, Procedure procedure, List<Construct> arguments)
	{
		if(procedure != null && player instanceof BukkitGamePlayer)
		{
			try
			{
				ParseTree tree = extractTree(procedure);
				Map<String, Construct> originalVars = extractOriginalArgs(procedure);
				Environment env = iterateAndOverwrite(getEnvironment(player), originalVars, arguments);

				Script.GenerateScript(tree, env.getEnv(GlobalEnv.class).GetLabel()).eval(tree, env);
			}
			catch (ConfigRuntimeException e)
			{
				printException(e);
			}
		}
	}

	public static void printException(ConfigRuntimeException e) {
		LoggerInterface logger = ArenaCore.getLogger();
		logger.broadcast("&8[&6ACore&8] &cException in CommandHelper! &e" + e.getSimpleFile() + ":&a" + e.getLineNum());
		logger.broadcast("&8[&6ACore&8] &c" + e.getExceptionType().getName() + ": &6" + e.getMessage());
	}

}
