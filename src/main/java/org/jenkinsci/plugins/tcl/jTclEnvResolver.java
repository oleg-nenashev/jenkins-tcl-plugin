/*
 * The MIT License
 *
 * Copyright 2013 Oleg Nenashev <o.v.nenashev@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.tcl;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.model.TaskListener;
import hudson.slaves.EnvironmentVariablesNodeProperty;
import hudson.slaves.NodeProperty;
import org.jenkinsci.plugins.tcl.interpreter.jTclException;
import tcl.lang.*;

import java.util.Map;

/**
 * Resolver for Jenkins variables.
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public class jTclEnvResolver implements Resolver {
    private TclDriver driver;
    private AbstractBuild build;
    private TaskListener listener;

    public jTclEnvResolver(TclDriver driver, AbstractBuild build, TaskListener listener) {
        this.driver = driver;
        this.build = build;
        this.listener = listener;
    }

    @Override
    public WrappedCommand resolveCmd(Interp interp, String s, Namespace namespace, int i) throws TclException {
        return null;
    }

    @Override
    public Var resolveVar(Interp interp, String s, Namespace namespace, int i) throws TclException {

        if (namespace.fullName.equals("::")) {
            // build parameters
            Map<String, String> buildVariables = driver.getBuildInfo().getBuildVariables();
            if (buildVariables.containsKey(s)) {
                return CreateConstScalarVar(interp, s, buildVariables.get(s));
            }

            // Environment parameters
            try {
                EnvVars envVars = build.getEnvironment(listener);
                if (envVars.containsKey(s)) {
                    return CreateConstScalarVar(interp, s, envVars.get(s));
                }
            } catch(Exception ex) {
                throw new jTclException("Can't process environment vars", ex);
            }
            
            // Get global properties
            for (NodeProperty nodeProperty: Hudson.getInstance().getGlobalNodeProperties()) {
                if (nodeProperty instanceof EnvironmentVariablesNodeProperty) {
                    EnvironmentVariablesNodeProperty vars = (EnvironmentVariablesNodeProperty)nodeProperty;
                    Var var = getVar(interp, s, vars.getEnvVars());
                    if (var!=null) return var;
                }
            }
            
            // Get node-specific global properties
            for (NodeProperty nodeProperty : build.getBuiltOn().getNodeProperties()) {
                if (nodeProperty instanceof EnvironmentVariablesNodeProperty) {
                                EnvironmentVariablesNodeProperty vars = (EnvironmentVariablesNodeProperty)nodeProperty;
                    Var var = getVar(interp, s, vars.getEnvVars());
                    if (var!=null) return var;
                }
            }
        }

        return null;
    }
    
    /**
     * Try to substitute variables from EnvVars
     * @param interp Tcl interpreter
     * @param varName Variable name
     * @param envVars  Collection of environment variables
     * @return Variable or null
     * @throws TclException Interpreter exception
     */
    private static Var getVar(Interp interp, String varName, EnvVars envVars) 
            throws TclException
    {
        if (envVars.containsKey(varName)) {
            return CreateConstScalarVar(interp, varName, envVars.get(varName));
        } 
        return null;
    }

    private static Var CreateConstScalarVar(Interp interp, String varName, String value)
            throws TclException {
        Var[] vars = Var.lookupVar(interp, "::" + varName, null, 0, null, true, true);
        Var res = null;

        for (int it = 0; it < vars.length; it++) {
            if (vars[it] != null) {
                if (res == null) {
                    res = vars[it];
                } else {
                    throw new jTclException(interp, "Lookup var must return only one result. Got array with size=" + vars.length);
                }
            }
        }

        Var.setVar(interp, "::" + varName, null, TclString.newInstance(value), 0);
        return vars[0];
    }
}
