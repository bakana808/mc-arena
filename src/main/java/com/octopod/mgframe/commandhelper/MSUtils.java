package com.octopod.mgframe.commandhelper;

import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.commandhelper.CommandHelperFileLocations;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.IVariableList;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.laytonsmith.database.SQLProfiles;
import com.octopod.octal.commandhelper.MethodScript;

import java.io.IOException;

/**
 * @author Octopod
 *         Created on 5/24/14
 */
public class MSUtils {

	/**
	 * Runs MethodScript, and returns the resultant Construct. (as the console by default)
	 * May throw ConfigCompileException during the compiling stage.
	 * @param script The MethodScript to run.
	 * @return Construct
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
	public static Construct execute(String script) throws ConfigCompileException {
		return executeAs(script, null, null);
	}

	/**
	 * Runs MethodScript, and returns the resultant Construct.
	 * May throw ConfigCompileException during the compiling stage.
	 * @param script The MethodScript to run.
	 * @param executor Who this script is going to be executed by.
	 * @return Construct
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
	public static Construct executeAs(String script, MCCommandSender executor) throws ConfigCompileException {
		return executeAs(script, null, executor);
	}

	public static Construct executeAs(String script, IVariableList variables, MCCommandSender executor) throws ConfigCompileException {

		MethodScript ms = new MethodScript(script);

		if(variables != null) {
			ms.setVariableList(variables);
		}
		if(executor != null) {
			ms.setExecutor(executor);
		}

		return ms.execute();

	}

    /**
     * Gets CommandHelper's default environment.
     * @return Environment
     */
    public static Environment createEnvironment() {

        CommandHelperPlugin plugin = CommandHelperPlugin.self;
        GlobalEnv gEnv;

        try {
            gEnv = new GlobalEnv(
                    plugin.executionQueue,
                    plugin.profiler,
                    plugin.persistenceNetwork,
                    plugin.permissionsResolver,
                    CommandHelperFileLocations.getDefault().getConfigDirectory(),
                    new SQLProfiles(CommandHelperFileLocations.getDefault().getSQLProfilesFile())
            );
        } catch (SQLProfiles.InvalidSQLProfileException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        gEnv.SetDynamicScriptingMode(true);
        CommandHelperEnvironment cEnv = new CommandHelperEnvironment();

        return Environment.createEnvironment(gEnv, cEnv);

    }

}
