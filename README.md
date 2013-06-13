jenkins-tcl-plugin
==================

Implements Tcl buildstep &amp; Co for jenkins

Features
---------
* Built-in Tcl virtual machine ([jTcl][1])
* Execution of Tcl scripts as a Build step (compatible with Tcl 8.4 with several exceptions)
* Access to Jenkins build parameters and environment vars from script

Wishlist
--------
* Tests
* Jenkins-specific instruction set (read-only access to build information, triggering jobs, etc.)
* Extension API (new commands, loggers, etc) 
* Jenkins Tcl CLI

License
--------
[MIT License][2]

[1]: http://jtcl.kenai.com/
[2]: http://www.opensource.org/licenses/mit-license.php
