# playground
A 'playground' for all kind of stuff...

The project can be compiled either on Linux or on Windows by calling 'mvn package'.

At this time it consist of a simple Enterprise Application with a single @Singleton/@Startup Bean which is calling two methods from a jboss-module which is also bundled with this project.

The tricky part is that the jboss-module contains a native 'C' part that is compiled out-off-the-box using Maven as well.

What you need to do after building the project is the following:

1. Install a JBoss EAP6 or EAP7 (for reference export JBOSS_HOME to the location of the installation)
2. Taken the project's root directory as 'SRCROOT', copy the following artifacts

        cp $SRCROOT/ear/target/playground.ear $JBOSS_HOME/standalone/deployments
        cp -a $SRCROOT/module/target/jboss-modules/org $JBOSS_HOME/modules

3. Start the JBoss server
     $JBOSS_HOME/bin/standalone.sh
4. You should see the following console output

        ...
        16:02:04,671 INFO  [org.jboss.playground.LogInModule] (ServerService Thread Pool -- 50) Hello World from Module
        16:02:04,673 INFO  [stdout] (ServerService Thread Pool -- 50) ---native-->
        
        
        Hello World from native code
        
        
        16:02:04,673 INFO  [stdout] (ServerService Thread Pool -- 50) <---native--
        ...

