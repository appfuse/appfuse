Quick-Start FAQ
===============

Use this file to specify features and important developer-related information
in your application.

To build this application - you must be using Ant 1.6.0+.  You will also need 
to copy junit.jar into your $ANT_HOME/lib directory.

Then setup Tomcat 4.1.x+ and install an smtp server on localhost.  If you don't 
want to install an SMTP server, change web/WEB-INF/classes/mail.properties to 
point to an existing one.

To run this application, you will need to perform the following tasks:

1.  The default database setup expects a mysql database installed with
    an admin user named "root" and no password.  If your system is different,
    modify properties.xml or build.properties to override this default.
2.  Run "ant setup-db".  This creates a mysql database named "appfuse" and
    grants the user "test" (password: test) full rights to this database.
3.  Test that the db access code works with:
    ant test-dao -Dtestcase=UserDAO
    ant test-service -Dtestcase=UserManager
4.  Setup Tomcat by running "ant setup".  This puts a mysql jdbc 
    driver (as well as JavaMail and jta.jar) in $CATALINA_HOME/common/lib, and
    also deploys an appfuse.xml file to $CATALINA_HOME/webapps ($CATALINA_HOME/
    conf/Catalina/localhost on Tomcat 5) and deploys the application.
5.  Start Tomcat and test the web login using:
    ant test-canoo -Dtestcase=Login
        
** TO SETUP E-MAIL NOTIFICATION OF ERRORS **
Log4j has a pretty slick feature where you can have e-mail messages sent
to you when an ERROR is logged.  To set this up, perform the following
steps:
  1. Change the property "error.mailTo" in build.properties to be your
     e-mail address.
  2. Edit WEB-INF/classes/log4j.properties to add "mail" to the rootCategory.
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
==+==                   http://raibledesigns.com/appfuse                  ==+==






