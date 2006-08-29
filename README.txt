AppFuse - Providing integration and style to open source Java. 
-----------------------------------------------------------------------------

Developer notes on setting up your IDE to work with AppFuse are contained 
below. Instructions currently existing for Eclipse and IDEA. Netbeans is
on the roadmap; you're more than welcome to create them in the meantime.

All issues should be logged in JIRA at http://issues.appfuse.org.
Talk with everyone on the mailing list at http://appfuse.org/forums.

Eclipse:
----------
Download Maven 2.0, install it, and add $M2_HOME/bin to your $PATH. From the 
command line, cd into the appfuse directory and type "mvn eclipse:eclipse". 
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

Once the project files have been created, open Eclipse and go to File > New >
Project > Java Project. Click Next and type "appfuse" in the Project name box. 
Click Finish to begin importing the project. 

To see how to rapidly develop using Eclipse and Jetty, see the following:

http://raibledesigns.com/page/rd?entry=edit_java_webapps_redux_jetty

IDEA:
----------
Download Maven 2.0, install it, and add $M2_HOME/bin to your $PATH. From the 
command line, cd into the appfuse directory and type "mvn idea:idea". Get a 
cup of coffee or soda (or even better, a beer!) while you wait for Maven 
to download all the dependencies.

After opening your project in IDEA, you may need to modify your Project JDK. 
