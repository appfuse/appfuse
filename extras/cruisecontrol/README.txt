To run CruiseControl on your AppFuse project, perform the following steps:

1. Download and install CruiseControl:
     http://prdownloads.sourceforge.net/cruisecontrol/cruisecontrol-2.2.zip?download
2. Modify cruise.sh to point to the installed directory.  If you're on Windows,
   rename cruise.sh to cruise.bat and point to the cruisecontrol.bat file.
3. Modify config.xml for your Ant path and publishing directories.  
4. Replace all the "ant" calls in build.xml with the one for your project, for
   example: <ant dir="checkout/yourproject" target="test-all"/>
5. Run "ant" in the current directory or checkout your project into the 
   "checkout" directory (you'll need to create it).  You can also run this on
   the "appfuse" project for testing purposes.
6. Create a "logs/appfuse" directory in the current directory.
7. Run "cruise.sh".
8. Tweak the publishers in config.xml to your heart's content.

