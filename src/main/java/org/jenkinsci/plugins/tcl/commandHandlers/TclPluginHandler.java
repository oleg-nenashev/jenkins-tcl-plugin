package org.jenkinsci.plugins.tcl.commandHandlers;

/**
 * Container of build-in Tcl commands
 */
public enum TclPluginHandler {
    GET_VERSION(TclGetVersion.class);

    // Internals
    ITclCommand command;

    TclPluginHandler(Class<? extends ITclCommand> commandClass) {
        try {
            command = commandClass.newInstance();
        } catch (Exception ex) {
            command = null;
        }
    }

    public ITclCommand getHandler() {
        return command;
    }
}
