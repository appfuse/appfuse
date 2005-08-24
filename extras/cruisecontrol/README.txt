To run CruiseControl on your AppFuse project, perform the following steps:

1. Download and install CruiseControl:
     http://prdownloads.sourceforge.net/cruisecontrol/cruisecontrol-2.2.zip?download
2. Modify cruise.sh to point to the installed directory.  If you're on Windows,
   rename cruise.sh to cruise.bat and point to the cruisecontrol.bat file.
3. Modify config.xml for your Ant path and publishing directories.  
4. Modify build.xml so it points to your CVS server and project.
5. Run "ant" in the current directory or checkout your project into the 
   "checkout" directory (you'll need to create it). 
6. Create a "logs/appfuse" directory in the current directory.
7. Run "cruise.sh".
8. See http://raibledesigns.com/wiki/AppFuseCruiseControl.html for how to
   setup CruiseControl to run as a daemon.

NOTE: If you're using Subversion instead of CVS, download svnant from
http://subclipse.tigris.org/servlets/ProjectDocumentList?folderID=1731. Put the 
directory into this directory, delete everything but it's "lib" directory and
adjust the path in build.xml accordingly.