JSF Integration Instructions
================================================================================

More information on my integration of JSF can be found at:

http://raibledesigns.com/page/rd?anchor=integrating_jsf_and_tapestry_into_appfuse

To install JSF as your web framework, you need to navigate to this
directory from the command line.  Then you can execute any of the following
targets with Ant. It might not be the most robust installer (it'll probably 
fail if run twice), but it seems to work good enough.

                install: Replaces Struts with JSF
                   help: Print this help text

================================================================================

Issues I encountered while developing the JSF Layer:

In other flavors of AppFuse, there's a security check when editing a user to
ensure that a non-admin is not trying to modify someone else's record.  With JSF, 
it's pretty hard to hack the URL (everything is a post), so I removed this check.

Saving a user form with validation errors results in the username and roles 
disappearing.

The passwordHint.jsp, reload.jsp and editProfile.jsp pages are all workarounds
for issues I encountered trying to call actions from non-JSF pages.  Mainly
the login page and Struts Menu - which doesn't currently support JSF actions.

Reload Options always returns to the Main Menu.  This is mainly to show success
messages, but also b/c redirecting to the referrer caused JSF to puke with 
a "response already committed" error.

It's nice that you don't have to create mappings in an XML file.  It's too bad
there's no control over where your files reside - i.e. WEB-INF/pages.
