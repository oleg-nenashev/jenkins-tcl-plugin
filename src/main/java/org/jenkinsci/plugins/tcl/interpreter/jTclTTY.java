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

import java.io.File;
import java.util.Random;

import hudson.FilePath;
import tcl.lang.Interp;
import tcl.lang.TclException;
import tcl.lang.channel.Channel;

/**
 * jTcl interpreter wrapper.
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public class jTclTTY {
    private Interp interp;
    private File tempDir;

    public jTclTTY() {
    }

    public Interp getInterpreter() {
        return interp;
    }

    protected void Initialize(FilePath workingDirectory, ClassLoader loader) throws TclException {
        tempDir = createTempDir();
        Thread.currentThread().setContextClassLoader(loader);
        interp = new Interp();
        interp.setVar("env", "TCL_CLASSPATH", "C:\\Users\\onenashev\\Documents\\Jenkins\\tcl-plugin\\tcl\\lib", 0);

        try {
            if (workingDirectory.exists() && workingDirectory.isDirectory()) {
                interp.setWorkingDir(workingDirectory.toString());
            } else {
                throw new jTclException("Working directory is invalid");
            }
        } catch (TclException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new jTclException("Working directory \"" + workingDirectory + "\" validation failed", ex);
        }
    }

    public String Execute(String command) throws TclException {
        interp.eval(command);
        return interp.getResult().toString();
    }

    public final File getWorkDirectory() {
        return interp.getWorkingDir();
    }

    public final File getTempDirectory() {
        return tempDir;
    }

    /**
     * Create a new temp directory within java.io.tmpdir.
     * Files created during tests will be written here.
     * <p/>
     * This file uses code from jTcl samples
     *
     * @return Path to the temporary directory.
     * @throws jTclException Temp Directory creation error
     */
    private File createTempDir() throws jTclException {
        //TODO: Add possibility of temp dir configuration
        String baseTempPath = System.getProperty("java.io.tmpdir");

        Random rand = new Random();
        int start = rand.nextInt() % 100000;
        if (start < 0) {
            start = -start;
        }
        for (int r = start; r < start + 10; r++) {
            File tempDirFile = new File(baseTempPath, "tcltest" + r);
            try {
                tempDirFile.mkdir();
                return tempDirFile;
            } catch (Exception e) {
                // ignore, keep trying until limit is reached
            }
        }
        throw new jTclException("Could not create temp directory in: " + baseTempPath);
    }

    /**
     * Overwrite default(std) IO channel.
     * @param type    Type of the channel
     * @param channel New Channel
     */
    public void setIOChannel(jTclChannelType type, Channel channel) {
        interp.interpChanTable.put(type.getChannelName(), channel);
    }

    public void setIOChannel(jTclChannel channel) {
        setIOChannel(channel.getType(), channel);
    }
}
