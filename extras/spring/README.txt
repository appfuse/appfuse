Spring MVC Integration Instructions
================================================================================

More information on my integration of Spring's MVC layer can be found at:

http://jroller.com/page/raible?anchor=migrating_from_struts_to_spring

To install Spring as your MVC framework, you need to navigate to this
directory from the command line.  Then you can execute any of the following
targets with Ant. It might not be the most robust installer (it'll create
duplicates if run twice), but it seems to work good enough.

                install: Replaces Struts with Spring MVC
                   help: Print this help text.

----
All of these targets simply parse build.xml and properties.xml to remove
Struts stuff and add Spring stuff.  

Some things I noticed in the process of developing this:

