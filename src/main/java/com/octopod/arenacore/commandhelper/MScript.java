package com.octopod.arenacore.commandhelper;

import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.core.*;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.Environment.EnvironmentImpl;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.ConfigCompileException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Octopod
 *         Created on 5/24/14
 */
public class MScript {

	/**
	 * The environment that the script will use.
	 */
    private Environment environment = null;

	/**
	 * If false, a clone of the environment will be used in place of the real one
	 * so as to not save potential environment changes.
	 */
	private boolean dynamicEnv = false;

	/**
	 * The procedures that this script has. Since the script needs to be executed
	 * once to get them, it will be if this variable is null.
	 * Executing this script at any time will also update this variable.
	 */
    private Map<String, Procedure> procs = null;

	/**
	 * The compiled MScript.
	 */
    private ParseTree compiled;

	private void compile(String ms, File source, Environment environment) throws ConfigCompileException
	{
		//Sets the source to UNKNOWN if null
		if(source == null) source = Target.UNKNOWN.file();

		//Sets the environment to a default environment if null
		if(environment == null) {
			this.environment = MScriptUtils.createEnvironment();
		} else {
			this.environment = environment;
		}

		compiled = MethodScriptCompiler.compile(MethodScriptCompiler.lex(ms, source, true));
	}

	/**
	 * Compiles MScript using a File as a source.
	 * @param source The source of the code, which will be shown if a CommandHelper exception is thrown
	 * @param script The MScript to compile
	 * @param env The enviroment in which the code will be run under
	 */
	public MScript(String script, File source, Environment env) throws ConfigCompileException
	{
		compile(script, source, env);
	}

	/**
	 * Compiles MScript using UNKNOWN as the source. (just like the interpreter)
	 * @param script The MScript to compile
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
    public MScript(String script) throws ConfigCompileException
	{
        compile(script, null, null);
    }

	/**
	 * Compiles MScript from a File, using the File as the source.
	 * @param source The file to read MScript from.
	 * @throws java.io.IOException
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
    public MScript(File source) throws IOException, ConfigCompileException
	{
        final StringBuilder script = new StringBuilder();

        BufferedInputStream input = new BufferedInputStream(new FileInputStream(source));

        int n;
        while((n = input.read()) != -1) {
            script.append((char)n);
        }

        try{
            input.close();
        } catch (IOException e) {}

        compile(script.toString(), source, null);
    }

	/**
	 * Sets the environment that this script will use.
	 * Following this method, all future executions will use this environment.
	 * @param env
	 */
    public void setEnvironment(Environment env) {
        environment = env;
    }

	/**
	 * Gets the environment that this script is going to use.
	 * If <code>environment</code> is null, then a clone of a default one will be used.
	 * @return the environment to be used.
	 */
    public Environment getEnvironment() {
		if(environment == null) environment = MScriptUtils.createEnvironment();
        return environment;
    }

	public Environment cloneEnvironment() {
		try {
			return getEnvironment().clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void setDynamicEnv(boolean dyn) {
		dynamicEnv = dyn;
	}

	public boolean isEnvironmentDynamic() {
		return dynamicEnv;
	}

    /**
     * Returns a map of procedures from the script. The script must be executed once for the environment
     * to cache the prodecures, so in case the cached list of procedures is null, the script will
     * be executed during this method.
     * @return a map of procedures from this script
     */
    public Map<String, Procedure> getProcedures() {
        if(procs == null) {execute();}
        return procs;
    }

	public Procedure getProcedure(String name) {
		return getProcedures().get(name);
	}

    /**
     * Include procedures into this script.
     * @param newProcs a map of procedures to include in this script.
     */
    public MScript include(Map<String, Procedure> newProcs) {
		Map<String, Procedure> procs = getGlobalEnvironment().GetProcs();
        procs.putAll(newProcs);
		getGlobalEnvironment().SetProcs(procs);
        return this;
    }

	/**
	 * Singular method of including procedures in this script.
	 * All procedure names must start with an underscore.
	 * @param name
	 * @param proc
	 * @return
	 */
    public MScript include(String name, Procedure proc) {
		Map<String, Procedure> procs = getGlobalEnvironment().GetProcs();
        procs.put(name, proc);
        getGlobalEnvironment().SetProcs(procs);
        return this;
    }

    /**
     * Tells the environment who's running the code.
     * It would modify what certain functions return such as player().
     * @param executor The new executor.
     */
    public MScript setExecutor(MCCommandSender executor) {
        getCmdHelperEnvironment().SetCommandSender(executor);
        return this;
    }

    /**
     * Sets a variable in the environment.
	 * All variable names must start with an "@" sign.
     * Unlike setVariableList(), it won't clear all the other variables.
     * @param varName The variable name. Should be prefixed by "@" most of the time.
     * @param con The Construct this variable should represent.
     */
    public MScript setVariable(String varName, Construct con) {
        IVariable var = new IVariable(varName, con, Target.UNKNOWN);
        IVariableList vars = getVariableList();
        vars.set(var);
        setVariableList(vars);
        return this;
    }

    /**
     * Gets a variable in the environment.
     * @param varName The variable name.
     */
    public Construct getVariable(String varName) {
        IVariableList vars = getVariableList();
        if(vars.keySet().contains(varName)) {
            return vars.get(varName, Target.UNKNOWN).ival();
        } else {
            return null;
        }
    }

    public String getSource() {
        return getTarget().file().toString();
    }

	public Target getTarget() {
		return compiled.getTarget();
	}

	public CommandHelperEnvironment getCmdHelperEnvironment() {
		return getEnvironment().getEnv(CommandHelperEnvironment.class);
	}

	public GlobalEnv getGlobalEnvironment() {
		return getEnvironment().getEnv(GlobalEnv.class);
	}

    /**
     * Sets the environment's variable list.
     * Using this may unintentionally clear all pre-existing variables,
     * so maybe you'd want to use setVariable() instead?
     * @param ivarList The IVariableList to use as the new variable list.
     */
    public MScript setVariableList(IVariableList ivarList) {
        getGlobalEnvironment().SetVarList(ivarList);
        return this;
    }

    /**
     * Gets the environment's variable list.
     * @return the IVariableList.
     */
    public IVariableList getVariableList() {
        return getGlobalEnvironment().GetVarList();
    }

    public Construct executeAs(MCCommandSender executor) {
        setExecutor(executor);
        return execute();
    }

	public Construct executeAs(MCCommandSender executor, Environment env)
	{
		env.getEnv(CommandHelperEnvironment.class).SetCommandSender(executor);
		return execute(env);
	}

    public Construct execute() {
        return execute(null, null);
    }

    public Construct execute(MethodScriptComplete done)
	{
		return execute(done, null);
    }

	public Construct execute(Environment env)
	{
		return execute(null, env);
	}

	/**
	 * Executes this code.
	 * @param done this will run after the code is done, can be null
	 * @param externalEnv the execution will use this environment if provided, can be null
	 * @return the Construct that results from this code
	 */
	public Construct execute(MethodScriptComplete done, Environment externalEnv)
	{
		Environment env;
		if(externalEnv == null)
		{
			//Use our environment
			env = dynamicEnv ? getEnvironment() : cloneEnvironment();
		} else {
			//Use the external environment; don't save procedures if this happens
			env = externalEnv;
		}

		MethodScriptCompiler.registerAutoIncludes(this.environment, null);
		Construct ret = MethodScriptCompiler.execute(compiled, env, done, null);

		if(externalEnv == null)
			procs = env.getEnv(GlobalEnv.class).GetProcs();

		return ret;
	}

	public Thread executeAsync(final MethodScriptComplete done) {
		Thread thread = new Thread()
		{
			public void run() {execute(done);}
		};
		thread.start();
		return thread;
	}

	public Thread executeAsync() {
		return executeAsync(null);
	}

    @Deprecated
    public MScript addEnvironmentImpl(EnvironmentImpl ienv) {
		environment = environment.cloneAndAdd(ienv);
        return this;
    }

}