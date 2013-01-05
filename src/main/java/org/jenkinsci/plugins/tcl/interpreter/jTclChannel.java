/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jenkinsci.plugins.tcl.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tcl.lang.Interp;
import tcl.lang.channel.StdChannel;

/**
 * Implements standard IO channels for Tcl interpreter.
 *
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public class jTclChannel extends StdChannel {
    InputStream input;
    OutputStream output;
    String name;
    jTclChannelType type;

    private jTclChannel(String name, jTclChannelType type) {
        super(type.getValue());
        this.name = name;
        this.type = type;
    }

    public jTclChannel(String name, jTclChannelType type, OutputStream output)
            throws jTclException {
        this(name, type);
        switch (type) {
            case STDERR:
            case STDOUT:
                this.output = output;
                break;
            default:
                throw new jTclException("Type " + type + " isn't supported for output stream");
        }
    }

    public jTclChannel(String name, jTclChannelType type, InputStream input)
            throws jTclException {
        this(name, type);
        switch (type) {
            case STDIN:
                this.input = input;
                break;
            default:
                throw new jTclException("Type " + type + " isn't supported for input stream");
        }
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        return input;
    }

    @Override
    protected OutputStream getOutputStream() throws IOException {
        return output;
    }

    @Override
    public String getChanName() {
        return name;
    }

    public jTclChannelType getType() {
        return type;
    }

}
