package org.jenkinsci.plugins.tcl.commandHandlers;

import tcl.lang.TclException;

/**
 * Tcl Command Handler interface. Supported by jTclCommandResolver
 * @see org.jenkinsci.plugins.tcl.interpreter.jTclCommandResolver
 */
public interface ITclCommand {

    /**
     * Execute command and return result
     * @param argStr
     * @return
     */
    public String Execute(String argStr) throws TclException;

    /**
     *
     * @return Command name
     */
    public String Name();
}
