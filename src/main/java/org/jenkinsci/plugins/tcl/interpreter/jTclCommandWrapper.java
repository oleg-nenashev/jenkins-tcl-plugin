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

import org.jenkinsci.plugins.tcl.commandHandlers.ITclCommand;
import tcl.lang.Command;
import tcl.lang.Interp;
import tcl.lang.TclException;
import tcl.lang.TclObject;

/**
 * Wrapper for internal Tcl Commands.
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 * @see ITclCommand
 */
public class jTclCommandWrapper implements Command {
    ITclCommand commandHandler;

    public jTclCommandWrapper(ITclCommand commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void cmdProc(Interp interp, TclObject[] tos) throws TclException {
        assert (tos.length >= 1);
        String commandName = jTclCommandResolver.extractFuncName(tos[0].toString());
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
        return jTclCommandResolver.extractFuncName(items[0]) +
                ((items.length == 2) ? " " + items[1] : "");
    }
}
