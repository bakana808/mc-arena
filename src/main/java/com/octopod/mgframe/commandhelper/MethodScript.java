package com.octopod.mgframe.commandhelper;

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
public class MethodScript {

    private Environment environment = MSUtils.createEnvironment();
    private Map<String, Procedure> procs = null;
    private ParseTree compiled;
	private Script script = new Script(null, null);

	private void compile(String ms, File source, Environment environment) throws ConfigCompileException
	{
		//Sets the source to UNKNOWN if null
		if(source == null) source = Target.UNKNOWN.file();

		//Sets the environment to a default environment if null
		if(environment == null) {
			this.environment = MSUtils.createEnvironment();
		} else {
			this.environment = environment;
		}

		compiled = MethodScriptCompiler.compile(MethodScriptCompiler.lex(ms, source, true));
	}

	/**
	 * Compiles MethodScript using a File as a source.
	 * @param source The source of the code, which will be shown if a CommandHelper exception is thrown
	 * @param script The MethodScript to compile
	 * @param env The enviroment in which the code will be run under
	 */
	public MethodScript(String script, File source, Environment env) throws ConfigCompileException
	{
		compile(script, source, env);
	}

	/**
	 * Compiles MethodScript using UNKNOWN as the source. (just like the interpreter)
	 * @param script The MethodScript to compile
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
    public MethodScript(String script) throws ConfigCompileException
	{
        compile(script, null, null);
    }

	/**
	 * Compiles MethodScript from a File, using the File as the source.
	 * @param source The file to read MethodScript from.
	 * @throws java.io.IOException
	 * @throws com.laytonsmith.core.exceptions.ConfigCompileException
	 */
    public MethodScript(File source) throws IOException, ConfigCompileException
	{
        final StringBuilder script = new StringBuilder();

        BufferedInputStream input = new BufferedInputStream(new FileInputStream(source));

        int n;
        while((n = input.read()) != -1) {
            script.append(n);
        }

        try{
            input.close();
        } catch (IOException e) {}

        compile(script.toString(), source, null);
    }

    public void setEnvironment(Environment env) {
        environment = env;
    }

    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Returns a map of procedures from the script. The script must be executed once for the environment
     * to cache the prodecures, so in case the cached list of procedures is null, the script will
     * be executed during this method.
     * @return a map of procedures from this script
     */
    public Map<String, Procedure> getProcedures() {
        if(procs == null) {
            procs = executeEnvironmentChange().getEnv(GlobalEnv.class).GetProcs();
        }
        return procs;
    }

    /**
     * Include procedures into this script.
     * @param newProcs a map of procedures to include in this script.
     */
    public MethodScript include(Map<String, Procedure> newProcs) {
        procs.putAll(newProcs);
        environment.getEnv(GlobalEnv.class).SetProcs(procs);
        return this;
    }

    public MethodScript include(String name, Procedure proc) {
        Map<String, Procedure> procs = getProcedures();
        procs.put(name, proc);
        getEnvironment().getEnv(GlobalEnv.class).SetProcs(procs);
        return this;
    }

    /**
     * Tells the environment who's running the code.
     * It would modify what certain functions return such as player().
     * @param executor The new executor.
     */
    public MethodScript setExecutor(MCCommandSender executor) {
        environment.getEnv(CommandHelperEnvironment.class).SetCommandSender(executor);
        return this;
    }

    /**
     * Sets a variable in the environment.
     * Unlike setVariableList(), it won't clear all the other variables.
     * @param varName The variable name. Should be prefixed by "@" most of the time.
     * @param con The Construct this variable should represent.
     */
    public MethodScript setVariable(String varName, Construct con) {
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
            return vars.get(varName, Target.UNKNOWN);
        } else {
            return null;
        }
    }

    public String getSource() {
        return compiled.getTarget().file().toString();
    }

	public CommandHelperEnvironment getCmdHelperEnvironment() {
		return environment.getEnv(CommandHelperEnvironment.class);
	}

	public GlobalEnv getGlobalEnvironment() {
		return environment.getEnv(GlobalEnv.class);
	}

    /**
     * Sets the environment's variable list.
     * Using this may unintentionally clear all pre-existing variables,
     * so maybe you'd want to use setVariable() instead?
     * @param ivarList The IVariableList to use as the new variable list.
     */
    public MethodScript setVariableList(IVariableList ivarList) {
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

    public Construct execute() {
        return execute(null);
    }

    public Construct execute(MethodScriptComplete done) {
        try {
            return MethodScriptCompiler.execute(compiled, environment.clone(), done, script);
        } catch (CloneNotSupportedException e) {
            return CVoid.VOID;
        }
    }

    /**
     * Executes the code to return the modified Environment after it has run.
     * @return the Environment after the code has run
     */
    private Environment executeEnvironmentChange() {
        Environment clone;
        try {
            clone = environment.clone();
            MethodScriptCompiler.execute(compiled, clone, null, script);
            return clone;
        } catch (CloneNotSupportedException e) {
            return environment;
        }

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
    public MethodScript addEnvironmentImpl(EnvironmentImpl ienv) {
		environment = environment.cloneAndAdd(ienv);
        return this;
    }



}