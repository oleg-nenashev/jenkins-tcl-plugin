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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tcl.lang.channel.StdChannel;

/**
 * Implements standard IO channels for Tcl interpreter.
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

    /**
     * Get type of IO channel.
     * @return Type of the TclChannel
     */
    public jTclChannelType getType() {
        return type;
    }

}
