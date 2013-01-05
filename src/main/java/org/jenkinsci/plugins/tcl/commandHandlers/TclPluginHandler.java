package org.jenkinsci.plugins.tcl.commandHandlers;

/**
 * Contains
 */
public enum TclPluginHandler {
    GET_VERSION(TclGetVersion.class);

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
