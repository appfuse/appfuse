WebWork Integration Instructions
================================================================================

More information on my integration of WebWork can be found at:

http://raibledesigns.com/page/rd?anchor=integrating_webwork_into_appfuse

To install WebWork as your web framework, you need to navigate to this
directory from the command line.  Then you can execute any of the following
targets with Ant. It might not be the most robust installer (it'll probably 
fail if run twice), but it seems to work good enough.

                install: Replaces Struts with WebWork
                   help: Print this help text.

================================================================================

Issues I encountered while developing the WebWork Layer:

Implementing ModelDriven on actions caused the getText() call on the label to 
fail.  Using the full object.fieldName syntax (w/o ModelDriven) solves this and
seems to be more in-line with how AppFuse works - since this syntax allows for
field labels to be looked up when validation fails.

WebWork's client-side visitor validation doesn't work with SpringObjectFactory.
Even if it did work, there's a couple of issues that make it a bit difficult
to use.  First of all, it shows one error at a time in a JavaScript alert - 
rather than all of them at once.  This can be tedious for a user as it makes
more sense to see all the errors at once.  Furthemore, the current 
implementation doesn't allow cancelling of client-side validation.

Validation messages seem to be added to a HashMap and rendered in a random
order.  Because of this, displaying the list of field errors at the top of
the page looks kinda funny.  I tried to fix this to no avail.
