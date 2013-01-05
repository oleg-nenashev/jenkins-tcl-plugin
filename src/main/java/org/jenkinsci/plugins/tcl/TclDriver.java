package org.jenkinsci.plugins.tcl;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import org.jenkinsci.plugins.tcl.interpreter.*;
import org.jenkinsci.plugins.tcl.util.StringOutputStream;
import tcl.lang.TclException;
import tcl.lang.Namespace;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: onenashev
 * Date: 02.01.13
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class TclDriver extends jTclTTY {
    public final static String JENKINS_NAMESPACE = "jenkins";

    private BuildListener listener;
    private Launcher buildLauncher;
    private AbstractBuild build;
    OblCommandResolver commandResolver;

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
    public TclDriver(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws TclException {
        this.build = build;
        this.buildLauncher = launcher;
        this.listener = listener;

        // Start init
        super.Initialize(build.getWorkspace(), getClass().getClassLoader());
        super.setIOChannel(new jTclChannel("jTCL_STDERR", jTclChannelType.STDERR, new StdErr()));
        super.setIOChannel(new jTclChannel("jTCL_STDOUT", jTclChannelType.STDOUT, new StdOut()));

        // Add resolver
        commandResolver = new OblCommandResolver(super.getInterpreter(), JENKINS_NAMESPACE);
        super.getInterpreter().addInterpResolver("JenkinsResolver", commandResolver);
        super.getInterpreter().addInterpResolver("Env resolver", new jTclEnvResolver(this));

        // Add jenkins namespace and resolver
        Namespace nm = Namespace.createNamespace(super.getInterpreter(), JENKINS_NAMESPACE, null);
        Namespace.setNamespaceResolver(nm, commandResolver);


        //TODO: add commands
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
