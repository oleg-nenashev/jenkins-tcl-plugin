package org.jenkinsci.plugins.tcl.commandHandlers;

/**
 * Created with IntelliJ IDEA.
 * User: onenashev
 * Date: 03.01.13
 * Time: 1:59
 * To change this template use File | Settings | File Templates.
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
