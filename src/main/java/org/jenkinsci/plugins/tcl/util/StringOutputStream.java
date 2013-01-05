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
     * Writes string to the stream
     *
     * @param str
     * @throws IOException
     */
    public abstract void WriteLine(String str) throws IOException;
}
