package com.octopod.arenacore.commandhelper;

import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.commandhelper.CommandHelperFileLocations;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.ParseTree;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.Profiles;
import com.laytonsmith.core.Profiles.InvalidProfileException;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.IVariable;
import com.laytonsmith.core.constructs.IVariableList;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.laytonsmith.core.taskmanager.TaskManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Octopod
 *         Created on 5/24/14
 */
public class MScriptUtils {

	/**
	 * Runs MScript, and returns the resultant Construct. (as the console by default)
	 * May throw ConfigCompileException during the compiling stage.
	 * @param script The MScript to run.
	 * @return Construct
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
	public static Construct eval(String script) throws ConfigCompileException {
		return eval(script, null, null);
	}

	/**
	 * Runs MScript, and returns the resultant Construct.
	 * May throw ConfigCompileException during the compiling stage.
	 * @param script The MScript to run.
	 * @param executor Who this script is going to be executed by.
	 * @return Construct
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
	public static Construct eval(String script, MCCommandSender executor) throws ConfigCompileException
	{
		return eval(script, null, executor);
	}

	public static Construct eval(String script, IVariableList variables, MCCommandSender executor) throws ConfigCompileException
	{
		MScript ms = new MScript(script);

		if(variables != null) {
			ms.setVariableList(variables);
		}
		if(executor != null) {
			ms.setExecutor(executor);
		}

		return ms.execute();
	}

	private static Environment defaultEnvironment = null;

    /**
     * Gets CommandHelper's default environment.
     * @return Environment
     */
    public static Environment createEnvironment()
	{
		if(defaultEnvironment != null) {
			return cloneEnvironment(defaultEnvironment);
		}

        CommandHelperPlugin plugin = CommandHelperPlugin.self;
        GlobalEnv gEnv;

        try
		{
            gEnv = new GlobalEnv(
                    plugin.executionQueue,
                    plugin.profiler,
                    plugin.persistenceNetwork,
                    plugin.permissionsResolver,
                    CommandHelperFileLocations.getDefault().getConfigDirectory(),
                    new Profiles(CommandHelperFileLocations.getDefault().getProfilesFile()),
					new TaskManager()
            );
        }
		catch (InvalidProfileException | IOException e)
		{
			return cloneEnvironment(defaultEnvironment);
		}

		gEnv.SetDynamicScriptingMode(true);
        CommandHelperEnvironment cEnv = new CommandHelperEnvironment();

        return defaultEnvironment = Environment.createEnvironment(gEnv, cEnv);

    }

	/**
	 * Attempts to clone the Environment, and if it fails, returns null.
	 * @param env
	 * @return
	 */
	public static Environment cloneEnvironment(Environment env)
	{
		try {
			return env.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public static Environment addVariable(Environment env, IVariable var) {
		IVariableList vars = env.getEnv(GlobalEnv.class).GetVarList();
		vars.set(var);
		env.getEnv(GlobalEnv.class).SetVarList(vars);
		return env;
	}

	public static Construct getVariable(Environment env, String varName) {
		IVariableList vars = env.getEnv(GlobalEnv.class).GetVarList();
		return vars.get(varName, Target.UNKNOWN).ival();
	}

	/**
	 * Extracts the ParseTree variable from a Procedure
	 * @param procedure
	 * @return
	 */
	public static ParseTree getProcedureTree(Procedure procedure)
	{
		try
		{
			Field field = procedure.getClass().getDeclaredField("tree");
			field.setAccessible(true);
			return (ParseTree)field.get(procedure);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Extracts the original variables from a Procedure
	 * @param procedure
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Construct> getProcedureVars(Procedure procedure) {
		try {
			Field field = procedure.getClass().getDeclaredField("originals");
			field.setAccessible(true);
			return (Map<String, Construct>)field.get(procedure);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
