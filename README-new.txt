Quick-Start FAQ
===============

Use this file to specify features and important developer-related information
in your application.

To build this application - you must be using Ant 1.5.1+ and have your 
$J2EE_HOME set to your J2EE SDK install directory.  You can also copy the
j2ee.jar file to lib/j2sdkee1.4/lib.  J2EE 1.4 is not required yet, so 
using a 1.3 lib is fine.  You can also pass in the parameter such as 
ant -Dj2ee.jar=/path/to/jar.  Make sure mail.jar and activation.jar are in
$J2EE_HOME/lib.

You will also need to copy junit.jar into your $ANT_HOME/lib directory.

Then setup Tomcat 4.1.x+ and install an smtp server on localhost.  If you
don't want to install an SMTP server, change mail.properties to point to an
existing one.

To run this application, you will need to perform the following tasks:

1.  Adjust the database.properties file to fit your system - the username
    and password for the database must have database/table create rights.
    It currently creates a mysql "appfuse" database using the user: root/admin.
    It also grants the user "test" (password: test) full rights to this 
    database.
2.  Run "ant setup-db".
3.  Test that the db access code works with:
    ant test-ejb -Dtestcase=UserDAOTest
    ant test-web -Dtestcase=UserManagerTest
4.  Setup Tomcat running "ant setup-tomcat deploy".  This puts a mysql jdbc 
    driver in $CATALINA_HOME/common/lib, puts a appfuse.xml file in 
    $CATALINA_HOME/webapps/ and deploys the application.
5.  Start Tomcat and test the web login using:
    ant test-canoo -Dtestcase=Login

** ATTENTION TOMCAT 5 USERS **
To run this application on Tomcat 5.0.4+, you need to make sure that you have
mail.jar, activation.jar (for the passwordHint feature) and jta.jar (for
Hibernate transactions) in your $CATALINA_HOME/common/lib directory.  For your
convenience, you can download these JARs from SourceForge where you downloaded
this release.  The file is named tomcat-5-jars.jar.

** ATTENTION TOMCAT 4 LE USERS **
To run this application on Tomcat 4.1.x LE, you need to make sure that you have
commons-collections.jar, commons-dbcp.jar and commons-pool.jar in your 
$CATALINA_HOME/common/lib directory.  For your convenience, you can download 
these JARs from SourceForge where you downloaded this release. The file is 
named tomcat-4-LE-jars.jar.  You will also need mail.jar and activation.jar
in the same directory.
        
** TO SETUP E-MAIL NOTIFICATION OF ERRORS **
Log4j has a pretty slick feature where you can have e-mail messages sent
to you when an ERROR is logged.  To set this up, perform the following
steps:
  1. Make sure mail.jar and activation.jar are in $CATALINA_HOME/common/lib.
  2. Change the property "error.mailTo" in build.properties to be your
     e-mail address.
  3. Edit WEB-INF/classes/log4j.properties to add "mail" to the rootCategory.
     Example: log4j.rootCategory=INFO, stdout, mail
	

What is AppFuse?
- It's an application that is meant to demonstrate how to use XDoclet with 
  Struts to generate your Forms, web.xml, struts-config.xml, and validation.xml.  
  It's also designed to show how you can use the different security packages 
  (i.e. form-based authentication, SSLExt) and advanced 
  Struts techniques (i.e. Tiles, Validator) to build your webapps.
  
Where do I put my own code?
- The build file is setup so that you can place your own packages anywhere 
  under the /src directory. For database access code, it's recommended that
  you put it under the src/ejb directory. 

What targets does the build file accept?
- Run "ant -projecthelp" for a complete list of available tasks.


==+== This file created by AppFuse, an open source project by Matt Raible ==+==






