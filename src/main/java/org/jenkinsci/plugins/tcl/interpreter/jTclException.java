package org.jenkinsci.plugins.tcl.interpreter;

import tcl.lang.TclException;

/**
 * TclException extension for usage in interpreter wrapper
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
