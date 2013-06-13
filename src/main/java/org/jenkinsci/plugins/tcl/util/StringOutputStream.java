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
package org.jenkinsci.plugins.tcl.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Implements OutputStream with internal buffering and per-string output
 */
public abstract class StringOutputStream extends OutputStream {

    String buffer;

    public StringOutputStream() {
        buffer = "";
    }

    @Override
    public void write(int i) throws IOException {
        char val = (char) i;
        switch (val) {
            case '\r':
                break;
            case '\n':
                DoWrite();
                break;
            default:
                buffer += val;
                break;
        }
    }

    private void DoWrite() throws IOException {
        WriteLine(buffer);
        buffer = "";
    }

    /**
     * Writes string to the stream.
     * @param str
     * @throws IOException
     */
    public abstract void WriteLine(String str) throws IOException;
}
