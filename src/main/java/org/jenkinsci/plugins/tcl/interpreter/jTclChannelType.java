/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jenkinsci.plugins.tcl.interpreter;

import tcl.lang.channel.StdChannel;

/**
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public enum jTclChannelType {
    STDERR(StdChannel.STDERR),
    STDOUT(StdChannel.STDOUT),
    STDIN(StdChannel.STDIN);

    int value;

    private jTclChannelType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getChannelName() {
        return toString().toLowerCase();
    }
}
