package org.jenkinsci.plugins.tcl;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import org.jenkinsci.plugins.tcl.interpreter.*;
import org.jenkinsci.plugins.tcl.util.StringOutputStream;
import tcl.lang.Interp;
import tcl.lang.TclException;
import tcl.lang.Namespace;

import java.io.*;

/**
 * Tcl driver that integrates jTclTTY with jenkins build environment
 */
//TODO: Incapsulate jTclTTY
public class TclDriver extends jTclTTY {
    public final static String JENKINS_NAMESPACE = "jenkins";

    private BuildListener listener;
    private Launcher buildLauncher;
    private AbstractBuild build;
    private Builder builderInstance;

    jTclCommandResolver commandResolver;

    public class StdErr extends StringOutputStream {
        @Override
        public void WriteLine(String str) throws IOException {
            TclDriver.this.listener.error(str);
        }
    }

    public class StdOut extends StringOutputStream {
        @Override
        public void WriteLine(String str) throws IOException {
            TclDriver.this.listener.getLogger().println(str);
        }
    }

    /**
     * Construct and initializes Tcl environment
     *
     * <p>
     * Function overwrites Thread.ClassLoader by Class.ClassLoader due to jenkins dependency loader specific
     * ( more info - http://jenkins.361315.n4.nabble.com/ClassLoader-in-plugins-td1470791.html )
     * </p>
     *
     * @param build
     * @param launcher
     * @param listener
     * @throws TclException Error occurred in jtcl or its wrapper
     */
    public TclDriver(AbstractBuild build, Launcher launcher, BuildListener listener, Builder buildInstance)
            throws TclException {
        this.build = build;
        this.buildLauncher = launcher;
        this.listener = listener;
        this.builderInstance = buildInstance;

        // Start init
        super.Initialize(build.getWorkspace(), getClass().getClassLoader());
        super.setIOChannel(new jTclChannel("jTCL_STDERR", jTclChannelType.STDERR, new StdErr()));
        super.setIOChannel(new jTclChannel("jTCL_STDOUT", jTclChannelType.STDOUT, new StdOut()));

        // Add resolver
        commandResolver = new jTclCommandResolver(super.getInterpreter(), JENKINS_NAMESPACE);
        super.getInterpreter().addInterpResolver("JenkinsResolver", commandResolver);
        super.getInterpreter().addInterpResolver("Env resolver", new jTclEnvResolver(this, build.getCharacteristicEnvVars()));

        // Add jenkins namespace and resolver
        Namespace nm = Namespace.createNamespace(super.getInterpreter(), JENKINS_NAMESPACE, null);
        Namespace.setNamespaceResolver(nm, commandResolver);


        //TODO: add built-in command handlers
    }

    @Override
    public String Execute(String command) throws TclException {
        try {
            return super.Execute(command);
        } catch (TclException ex) {
            if (ex.getMessage() == null) {
                Interp interp = super.getInterpreter();
                String msg;
                //TODO: move error decoding to jTclTTY
                if (interp.errorInfo != null) {
                    msg = "Error #" + interp.errorCode + ": " + interp.errorInfo;
                } else {
                    msg = interp.getResult().toString();
                }
                throw new jTclException(interp, msg);
            }
            throw ex;
        }
    }

    public BuildListener getBuildListener() {
        return listener;
    }

    public Launcher getBuildLauncher() {
        return buildLauncher;
    }

    public AbstractBuild getBuildInfo() {
        return build;
    }
}
