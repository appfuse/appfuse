Tapestry Integration Instructions
================================================================================

More information on my integration of Tapestry can be found at:

http://raibledesigns.com/page/rd?anchor=integrating_jsf_and_tapestry_into_appfuse

To install Tapestry as your web framework, you need to navigate to this
directory from the command line.  Then you can execute any of the following
targets with Ant. It might not be the most robust installer (it'll probably 
fail if run twice), but it seems to work good enough.

                install: Replaces Struts with Tapestry
                   help: Print this help text

================================================================================

Issues I encountered while developing the Tapestry Layer:

The Tapestry JAR contains patches for the popup calendar 
(http://nagoya.apache.org/jira/browse/TAPESTRY-222), global properties file 
(http://nagoya.apache.org/jira/browse/TAPESTRY-229) and friendly URLs 
(http://wiki.apache.org/jakarta-tapestry/FriendlyUrls) and allowing periods
in column names (http://nagoya.apache.org/jira/browse/TAPESTRY-234).

Adding a new user doesn't allow you to pick from the roles.  The 
userForm.setFrom("list") doesn't seem to be working in UserList.

Reload Options always returns to the Main Menu.  This is mainly to show success
messages, but also b/c redirecting to the referrer caused Tapestry to not display 
messages.