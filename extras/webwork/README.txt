WebWork Integration Instructions
================================================================================

More information on my integration of Spring's MVC layer can be found at:

http://jroller.com/page/raible?anchor=migrating_from_struts_to_spring

To install WebWork as your MVC framework, you need to navigate to this
directory from the command line.  Then you can execute any of the following
targets with Ant. It might not be the most robust installer (it'll probably fail
if run twice), but it seems to work good enough.

                install: Replaces Struts with WebWork
                   help: Print this help text.

================================================================================

Issues I encountered while developing the Spring MVC Layer:

Spring doesn't seem to have an equivalent of a default exception handler for 
all Controllers.  With Struts, I use an ActionExceptionHandler that catches
all exceptions from Actions, and forwards to their inputForward.  It works
pretty slick.  With Spring, your best bet is to use a 
SimpleMappingExceptionResolver (see petclinic sample app for an example), and
map individual exceptions to individual views.  You might also be able to use
an Interceptor to handle this.

I had to rename all the Tiles definitions - since there's no way to reference
the definitions directly from actions, I'm using the UrlFilenameViewController
to control many of the URL -> definition mappings.
