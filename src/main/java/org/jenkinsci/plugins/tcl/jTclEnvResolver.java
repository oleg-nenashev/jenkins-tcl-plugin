package org.jenkinsci.plugins.tcl;

import hudson.EnvVars;
import org.jenkinsci.plugins.tcl.interpreter.jTclException;
import tcl.lang.*;

import java.util.Map;

/**
 * Resolver for Jenkins variables
 */
public class jTclEnvResolver implements Resolver {
    private TclDriver driver;
    private EnvVars envVars;

    public jTclEnvResolver(TclDriver driver, EnvVars envVars) {
        this.driver = driver;
        this.envVars = envVars;
    }

    public WrappedCommand resolveCmd(Interp interp, String s, Namespace namespace, int i) throws TclException {
        return null;
    }

    public Var resolveVar(Interp interp, String s, Namespace namespace, int i) throws TclException {

        if (namespace.fullName.equals("::")) {
            // build parameters
            Map<String, String> buildVariables = driver.getBuildInfo().getBuildVariables();
            if (buildVariables.containsKey(s)) {
                return CreateConstScalarVar(interp, s, buildVariables.get(s));
            }

            // env parameters
            if (envVars.containsKey(s)) {
                return CreateConstScalarVar(interp, s, envVars.get(s));
            }
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
