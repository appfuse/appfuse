AppFuse Modular Spring MVC Archetype
--------------------------------------------------------------------------------
If you're reading this then you've created your new project using Maven and
appfuse-basic-spring.  You have only created the shell of an AppFuse Java EE
application.  The project object model (pom) is defined in the file pom.xml.
The application is ready to run as a web application. The pom.xml file is
pre-defined with Hibernate as a persistence model and Spring MVC as the web
framework.

There are two modules in this project. The first is "core" and is meant to 
contain Services and DAOs. The second is "web" and contains any web-related
files. Using this modular archetype is recommended when you're planning on
using "core" in multiple applications, or you plan on having multiple clients
for the same backend.

NOTE: Maven is currently unable to fully support multi-module archetypes.
Because of this limitation, the Java files are in the default package of
each module. It's recommended you follow AppFuse conventions for package
naming when you create your own source files. To track the status of this
Maven limitation, see http://jira.codehaus.org/browse/ARCHETYPE-23.

To get started, please complete the following steps:

1. Download and install a MySQL 5.x database from 
   http://dev.mysql.com/downloads/mysql/5.0.html#downloads.

2. From the command line, cd into the web directory and run "mvn jetty:run-war".

3. View the application at http://localhost:8080.

4. More information can be found at:

    http://appfuse.org/display/APF/QuickStart+Guide

