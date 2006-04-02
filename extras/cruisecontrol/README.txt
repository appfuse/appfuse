To automate the testing of your AppFuse project in CruiseControl 2.4.1, perform
the following steps:

1. Download and install CruiseControl:
     http://cruisecontrol.sourceforge.net/download.html
2. Copy build.xml and config.xml to your CruiseControl installation.
3. Modify config.xml for your e-mail address and project name.
4. Modify build.xml so it points to your CVS server and project.
5. Run "ant" in the current directory or checkout your project into the 
   "projects". 
6. Run cruisecontrol.bat (Windows) or cruisecontrol.sh (Unix).

Copy cruisecontrol-init.d to /etc/init.d/cruisecontrol on Linux if you'd like to
run it as a daemon.

NOTE: If you're using Subversion instead of CVS, download svnant from
http://subclipse.tigris.org/servlets/ProjectDocumentList?folderID=1731. Put the 
directory into this directory, delete everything but it's "lib" directory and
adjust the XML in build.xml accordingly.