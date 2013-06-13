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

import tcl.lang.TclException;

/**
 * TclException extension for usage in interpreter wrapper.
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public class jTclException extends TclException {
    Exception nestedEx;

    public jTclException(tcl.lang.Interp interp, java.lang.String msg) {
        super(interp, msg);
    }

    public jTclException(java.lang.String msg) {
        super(null, msg);
    }

    public jTclException(String message, Exception nested) {
        super(null, message + " Internal Exception+ " + nested.getMessage());
        nestedEx = nested;
    }

    public Exception getNestedException() {
        return nestedEx;
    }
}
