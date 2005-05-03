=============== Welcome to the AppFuse Development Environment ===============

This is a MyJavaPack installer for AppFuse 1.8.  It will prompt you for the 
packages you want to install, and then it will download and extract them to 
the directory you specify.  From there, you will need to specify ANT_HOME and 
CATALINA_HOME environment variables.  I encourage you to move the downloaded 
files to an  alternate location that you're more confortable with, for example:

	- $INSTALL_DIR/frameworks/appfuse -> c:\source\appfuse
	- $INSTALL_DIR/server/jakarta-tomcat-5.0.28 ->
		c:\tools\jakarta-tomcat-5.0.28

To run this installer, extract it to your hard drive and double-click the JAR 
or run:

	java -jar appfuse-installer.jar

Optional packages in this installer:

	AppFuse 1.8
	Ant 1.6.2
	Tomcat 5.0.28
	MySQL (Windows) 4.1.11