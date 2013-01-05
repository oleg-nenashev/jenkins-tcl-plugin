package org.jenkinsci.plugins.tcl;

import hudson.AbortException;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;
import tcl.lang.TclException;

import javax.servlet.ServletException;
import java.io.IOException;


/**
 * Implements builder for Tcl Scripts.
 * <p>
 * Supports execution of Tcl scripts and provides access to jenkins variables.
 * See TODOs for other functionality
 * </p>
 *
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public class TclScriptBuilder extends Builder {

    private String script;
    private static final String TEST_SCRIPT = "set a \"Hello, world!\"; puts $a;";

    @DataBoundConstructor
    public TclScriptBuilder(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws AbortException {

        try {
            TclDriver driver = new TclDriver(build, launcher, listener, this);
            String res = driver.Execute(script != null ? script : TEST_SCRIPT);
            if (res != null && res.length() > 0) {
                listener.getLogger().println(res);
            }
        } catch (TclException ex) {
            ex.printStackTrace();
            throw new AbortException("Tcl execution failed: " + ex.getMessage());
        }

        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link TclScriptBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     * <p/>
     * <p/>
     * See <tt>src/main/resources/hudson/plugins/hello_world/TclScriptBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        /**
         * Performs on-the-fly validation of the form field 'Script'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         */
        public FormValidation doCheckScript(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a script");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Execute Tcl script";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            save();
            //TODO: remove or add global configuration options
            return super.configure(req, formData);
        }
    }
}

