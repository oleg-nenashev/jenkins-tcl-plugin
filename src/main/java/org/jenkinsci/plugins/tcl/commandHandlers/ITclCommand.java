package org.jenkinsci.plugins.tcl.commandHandlers;

/**
 * Created with IntelliJ IDEA.
 * User: onenashev
 * Date: 02.01.13
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public interface ITclCommand {
    public String Execute(String argStr);

    public String Name();
}
