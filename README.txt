Quick-Start FAQ
===============

To install and configure AppFuse for development, see the QuickStart Guide at:
http://raibledesigns.com/wiki/AppFuseQuickStart.html. 

To learn how to develop your J2EE webapps with AppFuse, see 
http://raibledesigns.com/wiki/Articles.html (or docs/index.html if you 
downloaded the AppFuse source distribution). You can also download all the 
latest documentation to the "docs" directory by running "ant wiki".

To build this application - you must be using Ant 1.6.5+.  You will also need 
to copy junit.jar into your $ANT_HOME/lib directory.

You are expected to have Tomcat 5.0.x+ installed, as well as an SMTP server on 
localhost.  If you don't want to install an SMTP server, change 
web/WEB-INF/classes/mail.properties to point to an existing one.

To run this application, you will need to perform the following tasks:

 1. The default database setup expects a mysql database installed with
    an admin user named "root" and no password.  If your system is different,
    modify properties.xml or build.properties to override the default values.
 2. Run "ant setup-db".  This creates a mysql database named "appfuse" and
    grants the user "test" (password: test) full rights to this database.
 3  Test that the db access code works with:
    ant test-dao -Dtestcase=UserDAO
    ant test-service -Dtestcase=UserManager
 4. Setup Tomcat by running "ant setup-tomcat deploy". This deploys an 
    appfuse.xml file to $CATALINA_HOME/webapps ($CATALINA_HOME/conf/Catalina/
    localhost on Tomcat 5). It also deploys the application to 
    $CATALINA_HOME/webapps.
 5. Start Tomcat and test the web login using "ant test-canoo -Dtestcase=Login".
    If you experience issues with Tomcat, check your build/test/cargo.log file.


Features/Changes in 1.9.4 
============================
- Detailed JIRA Release Notes at http://tinyurl.com/y3rawn.
- Fixed issues with session not invalidated on logout (fix from Acegi 1.0.2).
- Changed Spring context files to new XSD syntax.
- Modified Spring MVC JSPs to use Spring 2.0 form tags.
- Fixed problem where menu links did not encode URLs properly - causing invalid
  sessions when cookies not enabled (http://issues.appfuse.org/browse/SM-64).
- Converted from JSP to Facelets for JSF/MyFaces option.
- Integrated Ajax4JSF into JSF/MyFaces option.
- Fixed issues with custom FreeMarker templates for WebWork forms.
- Modified XDoclet so [listener] elements are not longer generated in *.tld.
- Fixed various encoding issues with i18n files.
- Added support for PostgreSQL to iBATIS option.
- Dependent packages upgraded:
    * Acegi Security 1.0.2
    * EhCache 1.2.3
    * Hibernate 3.2.ga
    * iBATIS 2.2
    * jMock 1.1.0
    * MyFaces 1.1.4
    * MySQL JDBC Driver 5.0.3
    * Spring 2.0
    * Struts Menu 2.4.2
    * OSCache 2.3.2
    * WebTest build 1393
    * WebWork 2.2.4
    * XFire 1.2.2
- Dependent packages added:
    * Ajax4JSF 1.0.2
    * Facelets 1.1.11

Features/Changes in 1.9.3
============================
- Fixed UserSecurityAdvice so userCache is cleared properly when changing 
  username.
- Fixed versioning issues when updating a username with a pre-existing username
  or e-mail.
- Updated build.xml so DbUnit works with Microsoft SQL Express/Server.
- Removed geronimo-web.xml so AppFuse works out-of-the-box with Geronimo 1.1.
- Fixed WebWork and Struts server-side validation issues with file upload.
- Changed security.xml to explicitly protect admin-only pages.
- Added filter-mapping for securityFilter so Acegi protects DWR calls.
- Menu CSS improvements.
- Various i18n improvements.
- Dependent packages upgraded:
    * Acegi Security 1.0.1
    * EhCache 1.2.1
    * MyFaces Tomahawk 1.1.3
    * Struts Menu 2.4.1
    * XFire 1.1.2


Features/Changes in 1.9.2
============================
- Changed to use Mike Stenhouse's CSS Framework. Adapted winning themes from 
  CSS Design Contest for AppFuse and include 3 out-of-the-box.
- Added code coverage support using EMMA. Just type "emma" as one of you tasks
  when you want coverage. For example, "ant emma test-dao". Thanks to Mika 
  Göckel.
- Extended AppGen to allow for sub-packages. Thanks to Doug Hays.
- Add geronimo-web.xml and resin-web.xml to WEB-INF directory so AppFuse works 
  out-of-the-box on Resin or Geronimo 1.0.
- Added JavaScript and CSS Files to gzip compression url-patterns.
- Dependent packages upgraded:
    * Commons Logging 1.1
    * EhCache 1.2
    * Struts Menu 2.4
    * Struts 1.2.9
    * Spring 1.2.8
    * XFire 1.1
- Dependent packages added:
    * EMMA 2.0.5312    


Features/Changes in 1.9.1
============================
- Added database table generation support to AppGen with Middlegen. Now you're
  prompted to generate code from a POJO or a database table. Thanks to Bobby
  Diaz.
- Added iBATIS support to AppGen, including generic DAO and Manager. 
  Unfortunately, the SQLMap generation is tied to MySQL and last_insert_id(). 
  I blame iBATIS and it's lack of support for JDBC 3.0's getGeneratedKeys(). ;-)
- Created a version of CreateDAO tutorial for iBATIS. 
    http://raibledesigns.com/wiki/CreateDAOiBATIS.html
- Added Web Services support with an XFire installer in extras/xfire. Thanks to
  Mika Gšckel.
- Changed from using "username" as primary key to use traditional "id" property.
- Changed all *DAO classes to *Dao to be more consistent with Spring's 
  recommendations and API.
- Added prompt for web framework to "new" target.
- Fixed issues with AppFuse on Resin so it should now run out-of-the-box on 
  Resin 3.0.x (open source version).
- Fixed issues with AppGen and generating non-String types for ActionTests.
- Fixed issues with data entry of Chinese characters (re-arranged filter 
  mappings).
- Added Spanish and Chinese translations for DisplayTag messages.
- Added German translation. Thanks to Andreas Deininger.
- Changed "Cancel" button on list screens to use "Done".
- Dependent packages upgraded:
    * Acegi Security 1.0 RC2
    * DisplayTag 1.1
    * DWR 1.1.1
    * Hibernate 3.1.3
    * iBATIS 2.1.7
    * MySQL JDBC Driver 3.1.12
    * OSCache 2.3
    * PostgreSQL JDBC Driver 8.0-405
    * Spring 1.2.7
    * Tapestry 4.0.1
    * URL Rewrite Filter 3.0-beta
    * WebTest Build 1263
    * WebWork 2.2.2
- Dependent packages added:
    * XFire 1.0


Features/Changes in 1.9
============================
- Changed Remember Me and SSL Switching to use Acegi Security instead of 
  homegrown solution. All users are encouraged to change the "key" property
  in applicationContext-security.xml (in the rememberMeServices and 
  rememberMeAuthenticationProvider bean definitions) to a unique key for
  their application.  More information on this change at:
    http://raibledesigns.com/page/rd?entry=using_acegi_security_for_remember
- Modified Acegi configuration to use UserDAO instead of Acegi classes for
  looking up the user's information. This change includes the ability
  to set a number of new account-related settings for the user.
- Changed "dataSource" bean to be a connection pool configured by Spring 
  and DBCP rather than one looked up from JNDI (increases portability and ease 
  of deployment). How to switch back to JNDI is documented at:
    http://tinyurl.com/7fzxo
- Added DWR, Prototype and Scriptaculous to allow easy development of 
  Ajax-enabled applications.
- Enhancements to Canoo WebTest tests so JavaScript is enabled when testing
  all web frameworks - not just JSF and Tapestry.
- Added LocaleFilter to allow users to set the locale with a parameter or
  programmatically.
- UI Enhancements: replaced expandable menu with Cool Menus 4, re-worked Login
  screen to be more professional, added JavaScript to focus on the first
  field in any screen that has forms.
- Added ability to pre-compile JSPs as part of build process.
- Created Hibernate Relationships tutorial showing how to setup the different
  types of relationships with XDoclet and Struts.
- Changed Boolean types in User.java to boolean as this seems to be more
  portable between databases - as well as easier to use when writing code.
- Fixed issue where Tapestry contrib:Table would through exceptions when 
  clicking on the column headers (to sort) in rapid succession.
- Removed XDoclet generation of web.xml.
- Added Italian Translation of messages.
- Changed default user role from "tomcat" to "user".
- Dependent packages upgraded:
    * Acegi Security 1.0 RC1
    * Cargo 0.7
    * Commons Validator 1.2.0
    * Log4j 1.2.11
    * Hibernate 3.1
    * iBATIS 2.1.6
    * JSTL 1.1.2 (only used when jsp.2=true)
    * MyFaces 1.1.1
    * MySQL JDBC Driver 3.1.11
    * OSCache 2.2
    * PostgreSQL JDBC Driver 8.0-404
    * Spring 1.2.6
    * Struts 1.2.8
    * URL Rewrite Filter 2.6.0
    * WebTest Build 1108
    * XDoclet 1.3 Nightly
- Dependent packages added:
    * DWR 1.0
    * Prototype 1.4.0
    * Scriptaculous 1.5.1
    * CoolMenus 4

    
Features/Changes in 1.8.2
============================
- A complete list of issues can be read from JIRA at http://tinyurl.com/aumf5.
- Added support for running tests and webapp in debug mode.
    http://issues.appfuse.org/browse/APF-87
- Security fix: prevent regular users from upgrading to admin role.
    http://issues.appfuse.org/browse/APF-96
- Security fix: don't allow regular users to view list of users.
    http://issues.appfuse.org/browse/APF-88
- Changed Acegi Security Filter mapping so JavaScript, CSS and image files
  are not processed.
- Enhanced AppGen to support inheritance and better default data for DBUnit.
- Added Prototype and script.aculo.us JavaScript libraries, removed "yellow
  fade technique" because of problems with Canoo WebTest. Removed fade.js b/c
  of issues with IE and displaytag.
- Updated all existing and generated Spring XML for Spring 1.2's syntax.
- [Spring MVC] Fixed issue with Dates in objectToRequestParameters() method.
    http://issues.appfuse.org/browse/APF-127
- Commented out generated Query by Example code in generated Hibernate DAOs b/c
  it caused more problems than it solved.
- Changed BaseDAOTestCase to extends Spring's 
  AbstractDependencyInjectionSpringContextTests.
    http://issues.appfuse.org/browse/APF-129
- Dependent packages upgraded:
    * Acegi Security 0.8.3
    * Spring 1.2.4
    * Spring Modules Validator Nightly (20050727)
    * URL Rewrite Filter 2.5.1

Features/Changes in 1.8.1
============================
- A complete list of issues fixed is at http://tinyurl.com/8v2z9.
- i18n fixes for DateUtil and Canoo WebTest.
- Moved tomcat.properties key/value pairs into properties.xml.
- [Struts] Changed "viewUsers" forward to be a redirect to prevent the 
  duplicate post problem when deleting a user.
- [MyFaces] Changed all "id" attributes with a period (i.e. address.address)
  to use only the later half since MyFaces 1.0.9 does support periods in ids.
- Added page for sponsors at http://appfuse.dev.java.net/sponsors.html
- Added UserSecurityAdvice and interceptor to ensure that only admins
  can modify user records. Regular users can still modify their own record.
- Fixed BaseControllerTestCase.objectToRequestParameters() so POJOs that
  use inheritance are supported.
- Dependent packages upgraded:
    * Cargo 0.5
    * Hibernate 3.0.5
    * iBATIS 2.1.0
    * MyFaces 1.0.9
    * Rename Packages 1.1
    * Spring 1.2.1


Features/Changes in 1.8
============================
- Replaced Container-Managed Authentication with Acegi Security Framework for
  Spring.  Minimal changes were necessary - meaning that you could easily 
  switch back if you wanted to.  HowTo documented at:
    http://raibledesigns.com/wiki/Wiki.jsp?page=AppFuseSecurity
- Massive amount of bug fixes applied to AppGen to allow it to be run multiple
  times. Also fixed "id" problems so the POJOs id property is actually looked
  up instead of just assuming it's a Long and named "id".
- Fixed project files for Eclipse and IDEA so you can easily run unit tests
  from within your IDE. Just run the "compile" task with Ant, refresh your
  project and test away!
- [AppGen] Added Query by Example code to the getXXXs(XXX) method of detailed
  Hibernate DAOs. Added code to Struts and Spring versions to allow searching
  on the list screen by manually passing in name/value pairs of a POJO's 
  properties on the URL. Doesn't seem to work for filtering by primary key,
  which seems reasonable since you'll likely hide the primary key column when
  modifying your list screen.
- [Tapestry] Fixed bugs in UserForm.java and UserList.java classes - now role
  choices are shown when editing a user or adding a new user.
- Fixed bug where uses could hack the URL in Struts and WebWork versions to 
  delete users when they don't have the "admin" role.
    https://appfuse.dev.java.net/issues/show_bug.cgi?id=121
- Added empty "velocimacro.library" property to "velocityEngine" bean in 
  applicationContext-service.xml to suppress Velocity errors.
- Changed Transaction Attributes on UserManager so save* rolls back when 
  the checked UserExistsException is thrown.
- [Struts] Fixed bug in error.jsp that caused it to throw exceptions under  
  certain conditions: https://appfuse.dev.java.net/issues/show_bug.cgi?id=122.
- [Struts] Modified struts_form.xdt to properly handle Sets when used for
  indexed properties.
- Added serialVersionUID variables to all serializable classes.
- Changed IDEA project files so project is loaded as a web module. This 
  allows Tomcat debugging and taglib resolution in JSPs.
- Increased the default font size of success/error messages since all my 
  clients have asked me to it when developing a project with AppFuse.
- Added fade.js for using the Fade Anything Technique as documented at
  http://www.axentric.com/posts/default/7.  Applied fading to messages.
- Improved build.xml file:
    * create-tables.sql now deleted when running "clean" target.
    * "fixcrlf" target updated to repair more file types on *nix systems.
    * Placeholders replaced in applicationContext-hibernate.xml within the 
      DAO JAR file created by "package-dao" target.
    * Simplified token replacement when deploying static web files.
    * Improved name replacement when running "new" target.
    * Replaced "compile-module" and "test-module" with macrodefs.
    * Changed entity include for properties.xml to be an import.
    * Moved app-settings.xml properties into properties.xml.
    * Changed from 80 characters per line to 120.
- Tests using Dumbster updated to always use "localhost" in case the normal
  mail.host is different.
- [Struts] UserAction updated to validate the form within the save() method
  to better support i18n.
- Removed unnecessary "database.name" property from PostgreSQL configuration
  in build.properties.
- Improved sensitivity for package name replacement in 
  ConvertUtil.getOpposingObject().
- Fixed hard-coded date format in DateUtil using Spring's LocaleContextHolder.
- [Struts] Changed all "bean:write" tags to "c:out" to prevent errors when
  using an unsupported locale.
- [Struts] Added TimestampConverter to converting Timestamps.
- [Spring MVC] BaseFormController updated to make the userManager available to
  subclasses.
- Updated LoginServlet to properly handle empty port configurations.
- [Spring MVC] cancelView property added to BaseFormController.
- Added property to userList DisplayTag to improve browser support for PDF 
  export.
- Dependent packages upgraded:
    * Display Tag 1.0
    * Dumbster 1.5
    * Hibernate 3.0.2
    * Log4j 1.2.9
    * MySQL JDBC Driver 3.1.7
    * PMD 3.0
    * PostgreSQL JDBC Driver 8.0-310
    * SiteMesh 2.2.1
    * Spring 1.2 RC2
    * StrutsTestCase 2.1.3
    * XDoclet 1.2.3
- Dependent packages added:
    * Acegi Security 0.8.2
- Dependent packaged removed:
    * JUnitDoclet 1.0.2

###
