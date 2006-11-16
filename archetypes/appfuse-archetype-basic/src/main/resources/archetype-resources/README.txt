AppFuse Basic Archetype
-----------------------------------------------------------------------------
If you're reading this then you've created your new project using Maven and
appfuse-archetype basic.  You have only created the shell of an AppFuse JEE
application.  You will need to modify pom.xml in the project directory to
define your architecture dependencies.  A lot of the project file sections
were taken from the Maven project file for AppFuse development.  You will want
to modify and customize your file to suit your project needs.

Developer notes on setting up your IDE to work with AppFuse are contained 
below. Instructions currently existing for Eclipse and IDEA. Netbeans is
on the roadmap; you're more than welcome to create them in the meantime.

To begin, download Maven 2.0.4, install it, and add $M2_HOME/bin to your $PATH.
Create a MAVEN_OPTS environment variable and set its value to:

-Xms512M -Xmx512M

All issues should be logged in JIRA at http://issues.appfuse.org.
Get your questions answered on the mailing list at http://appfuse.org/forums.

Eclipse 3.2+:
----------
From the command line, cd into the appfuse directory and run:

mvn eclipse:eclipse -DdownloadSources=true

Get a cup of coffee or soda (or even better, a beer!) while you wait for Maven 
to download all the dependencies.

Eclipse needs to know the path to the local maven repository. Therefore the 
classpath variable M2_REPO has to be set. Execute the following command:

mvn -Declipse.workspace=<path-to-eclipse-workspace> eclipse:add-maven-repo 

You can also define the M2_REPO classpath variable inside Eclipse. From the 
menu bar, select Window > Preferences. Select the Java > Build Path > Classpath 
Variables page. Add a new one with a name of M2_REPO and Path of to your local 
Maven repository (/Users/${username}/.m2/repository on OS X and 
C:\Documents and Settings\${username}\.m2\repository on Windows). 

To setup hierarchical projects in Eclipse 3.2+, perform the following steps:

1. Rename appfuse/data/.project file to something else. 
2. Go to File -> Import ->  General -> Existing Projects and browse to your 
   workspace and the appfuse/data root. Because there is no .project file, 
   Eclipse will show all the subprojects as being available.  Select them and 
   click ok.
3. Rename the appfuse/data/.project back and refresh your left pane in Eclipse.
4. Repeat these steps for the "web" directory.

IDEA 6.0+:
----------
From the command line, cd into the appfuse directory and type "mvn idea:idea". 
Get a cup of coffee or soda (or even better, a beer!) while you wait for Maven 
to download all the dependencies.

After opening your project in IDEA, you may need to modify your Project JDK. 
