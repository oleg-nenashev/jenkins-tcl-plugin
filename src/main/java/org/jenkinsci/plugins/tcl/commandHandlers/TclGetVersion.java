package org.jenkinsci.plugins.tcl.commandHandlers;

/**
 * Command sample
 */
public class TclGetVersion implements ITclCommand {
    public static final String COMMAND_NAME = "GetVersion";
    public static final String PLUGIN_VERSION = "0.1";

    public String Execute(String argStr) {
        return PLUGIN_VERSION;
    }

    public String Name() {
        return COMMAND_NAME;
    }
}
