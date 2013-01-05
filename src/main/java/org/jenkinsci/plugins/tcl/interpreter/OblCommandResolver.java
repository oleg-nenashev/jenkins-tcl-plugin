/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jenkinsci.plugins.tcl.interpreter;

import java.util.HashMap;

import org.jenkinsci.plugins.tcl.commandHandlers.ITclCommand;
import tcl.lang.Interp;
import tcl.lang.Namespace;
import tcl.lang.Resolver;
import tcl.lang.TclException;
import tcl.lang.Var;
import tcl.lang.WrappedCommand;

/**
 * Implements jtcl command resolver.
 *
 * <p>
 * Commands can be registered via registerCommand()
 * All commands are stored at one namespace.
 * </p>
 *
 * @see ITclCommand
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public class OblCommandResolver implements Resolver {
    private String commandNamespace;
    private tcl.lang.Interp tclInterp;
    private HashMap<String, ITclCommand> commandSet;
    private HashMap<String, Var> variableSet;

    public OblCommandResolver(tcl.lang.Interp interp, String namespace) {
        tclInterp = interp;
        commandNamespace = namespace;
        commandSet = new HashMap<String, ITclCommand>();
        variableSet = new HashMap<String, Var>();
    }

    public void RegisterCommand(ITclCommand command) throws TclException {
        if (commandSet.containsKey(command.Name())) {
            throw new TclException(tclInterp, "Key " + command.Name() + " has been already registered");
        }

        // commandSet.put(command.Name(), command);
    }

    public void UnregisterCommand(ITclCommand command) {
        if (commandSet.containsKey(command.Name())) {
            commandSet.remove(command.Name());
        }
    }

    public WrappedCommand resolveCmd(Interp interp,
                                     String string, Namespace nmspc, int i) throws TclException {
        if (!checkNamespace(string, nmspc))
            return null;

        String funcName = extractFuncName(string);
        if (commandSet.containsKey(funcName)) {
            WrappedCommand res = new WrappedCommand();
            res.cmd = new OblCommandExecutor(commandSet.get(funcName));
            return res;
        } else return null;
    }

    public Var resolveVar(Interp interp,
                          String string, Namespace nmspc, int i) throws TclException {
        //TODO: add support of variables

        return null;
    }

    public boolean checkNamespace(String command, Namespace nmspc) {
        //TODO: Support hierarchical namespaces
        String[] items = command.split("::");

        // Current namespace
        if (nmspc.name.equals(commandNamespace) && items.length == 1)
            return true;

        // Access from global
        if (nmspc.fullName.equals("::")
                && items.length == 2
                && items[0].equals(commandNamespace)
                ) {
            return true;
        }

        // Miss
        return false;
    }

    static String extractFuncName(String command) {
        String[] items = command.split("::");
        assert (items.length > 0);
        return items[items.length - 1];
    }
}
