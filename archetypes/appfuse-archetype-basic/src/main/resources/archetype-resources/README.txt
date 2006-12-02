AppFuse Basic Archetype
-----------------------------------------------------------------------------
If you're reading this then you've created your new project using Maven and
appfuse-archetype basic.  You have only created the shell of an AppFuse JEE
application.  The project object model (pom) is defined in the file pom.xml.
The application is web-ready using AppFuse static snapshot deployments. The
pom file is pre-defined with hibernate as a persistence model and Spring MVC
as the web MVC model.

You may need an appfuse database. If so you will have to do the
following:

1.  Download and install MySQL 5.x database
2.  Run http://static.appfuse.org/downloads/appfuse.sql

    e.g.
        $mysql -u root -p < appfuse.sql

3.  Cd to your new project root directory and run the application hosted
    at http://localhost:8080/

    e.g.
        $mvn jetty:run-war


4.  More information can be found at

    http://dev.appfuse.org/display/APF/QuickStart+Guide

