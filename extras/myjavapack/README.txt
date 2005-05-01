To download and create the MyJavaPack installer for AppFuse, simply run
"ant -f download.xml".  This will download MyJavaPack, install it, and create
the installer based on the settings in build.xml and install.xml. To disable
download and installation of MyJavaPack, use "ant -f download.xml 
-Ddownload=false".