/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jenkinsci.plugins.tcl.interpreter;

import org.jenkinsci.plugins.tcl.commandHandlers.ITclCommand;
import tcl.lang.Command;
import tcl.lang.Interp;
import tcl.lang.TclException;
import tcl.lang.TclObject;

/**
 * Wrapper for internal Tcl Command
 *
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 * @see ITclCommand
 */
public class OblCommandExecutor implements Command {
    ITclCommand commandHandler;

    public OblCommandExecutor(ITclCommand commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void cmdProc(Interp interp, TclObject[] tos) throws TclException {
        assert (tos.length >= 1);
        String commandName = OblCommandResolver.extractFuncName(tos[0].toString());
        String command = commandName;

        for (int i = 1; i < tos.length; i++) {
            command += " " + tos[i].toString();
        }

        String res = commandHandler.Execute(command);
        interp.setResult(res);
    }

    //TODO: Refactor during API modification
    @Deprecated
    static String extractCommandCall(String command) {
        String[] items = command.split("\\s+", 2);
        assert (items.length > 0 && items.length < 3);
        return OblCommandResolver.extractFuncName(items[0]) +
                ((items.length == 2) ? " " + items[1] : "");
    }
}
