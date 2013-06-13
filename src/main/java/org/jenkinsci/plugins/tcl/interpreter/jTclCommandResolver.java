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
 * Commands can be registered via registerCommand()
 * All commands are stored at one namespace.
 *
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 * @see ITclCommand
 */
public class jTclCommandResolver implements Resolver {
    private String commandNamespace;
    private tcl.lang.Interp tclInterp;
    private HashMap<String, ITclCommand> commandSet;
    private HashMap<String, Var> variableSet;

    public jTclCommandResolver(tcl.lang.Interp interp, String namespace) {
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

    @Override
    public WrappedCommand resolveCmd(Interp interp,
                                     String string, Namespace nmspc, int i) throws TclException {
        if (!checkNamespace(string, nmspc))
            return null;

        String funcName = extractFuncName(string);
        if (commandSet.containsKey(funcName)) {
            WrappedCommand res = new WrappedCommand();
            res.cmd = new jTclCommandWrapper(commandSet.get(funcName));
            return res;
        } else return null;
    }

    @Override
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
