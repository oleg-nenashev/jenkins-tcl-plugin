package org.jenkinsci.plugins.tcl;

import org.jenkinsci.plugins.tcl.interpreter.jTclException;
import tcl.lang.*;

import java.util.Map;

/**
 * Resolver for Jenkins variables
 */
public class jTclEnvResolver implements Resolver {
    private TclDriver driver;

    public jTclEnvResolver(TclDriver driver) {
        this.driver = driver;
    }

    public WrappedCommand resolveCmd(Interp interp, String s, Namespace namespace, int i) throws TclException {
        return null;
    }

    public Var resolveVar(Interp interp, String s, Namespace namespace, int i) throws TclException {
        Map<String, String> buildVariables = driver.getBuildInfo().getBuildVariables();

        if (namespace.fullName.equals("::") && buildVariables.containsKey(s)) {
            Var[] vars = Var.lookupVar(interp, "::" + s, null, 0, null, true, true);
            Var res = null;

            for (int it = 0; i < vars.length; i++) {
                if (vars[i] != null) {
                    if (res == null) {
                        res = vars[i];
                    } else {
                        throw new jTclException(interp, "Lookup var must return only one result. Got array with size=" + vars.length);
                    }
                }
            }

            Var.setVar(interp, "::" + s, null, TclString.newInstance(buildVariables.get(s)), 0);
            return vars[0];
        }

        return null;
    }
}
