Quick-Start FAQ
===============

To install and configure AppFuse for development, see the QuickStart Guide at:
http://raibledesigns.com/wiki/Wiki.jsp?page=AppFuseQuickStart. 

To learn how to develop your J2EE webapps with AppFuse, see 
http://raibledesigns.com/wiki/Wiki.jsp?page=Articles (or docs/index.html if you 
downloaded the AppFuse source distribution). You can also download all the 
latest documentation to the "docs" directory by running "ant wiki".

To build this application - you must be using Ant 1.6.2+.  You will also need 
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


Features/Changes in 1.7
============================
- Added support for JSF (MyFaces) and Tapestry as web framework options.
    http://raibledesigns.com/page/rd?anchor=integrating_jsf_and_tapestry_into_appfuse
- Added support for exporting PDFs from a displaytag-rendered table.
- Changed URIs for imported XML files in build.xml to use relative paths.
    https://appfuse.dev.java.net/issues/show_bug.cgi?id=102
- Fixed package-name bug when generating a new WebWork projects.
    https://appfuse.dev.java.net/issues/show_bug.cgi?id=101
- Added "fixcrlf" target for Unix machines to run when they're having issues 
    installing packages in the "extras" folder.
- Fixed bug in struts_form.xdt where nested objects types where named based 
    on the variable name rather than the type.
    https://appfuse.dev.java.net/issues/show_bug.cgi?id=105
- Dependent packages upgraded:
    * Cargo 0.4
    * DisplayTag 1.0 RC2
    * Hibernate 2.1.7
    * iBATIS 2.0.8
    * Spring 1.1.3-dev 
        ** Would have done 1.1.2, except the following bug exists:
           http://forum.springframework.org/viewtopic.php?t=2118
    * WebTest build 574
    * WebWork 2.1.6
    

Features/Changes in 1.6.1
============================
- Changed Service Tests to use JMock for mocking DAO dependencies.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=86
- Re-arranged filters-mappings in metadata/web/filter-mappings.xml to 
  fix exporting Excel with Display Tag. 
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=74
- Fixed LabelTag to read JSTL's fallback locale from web.xml.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=75
- Changed copy-resources task to only run "native2ascii" task on 
  Chinese properties files. 
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=76
- Fixed XDoclet form-generation template (struts_form.xdt) for generating
  nested forms.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=77
- Moved exportFilter and compressionFilter filter-mappings to come after
  encodingFilter to solve NoSuchMethodException when using zh_CN locale.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=79
- [Spring MVC] Fixed JavaScript bug on signup page.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=80
- Enhancements to test/web-tests.xml to be more i18n friendly.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=81
- Fixed bug in Commons Validator support for Spring. An alternative
  solution is to put the validator beans in applicationContext-service.xml.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=82
- [Spring MVC] Fixed server-side validation for Edit Profile when logged
  in as "tomcat" user.
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=83
- Fixed applicationContext-*.xml path to load each file individually so
  deploying AppFuse as a WAR works.  Will revert back after next release
  of Spring. https://appfuse.dev.java.net/issues/show_bug.cgi?id=85
- Changed UserManager.saveUser to throw checked UserExistsException when 
  DataIntegrityViolationException occurs.
- Patched XDoclet so that running "ant setup test-all" will work again.
  http://opensource.atlassian.com/projects/xdoclet/browse/XDT-879 
- Added CruiseControl files and documentation to extras/cruisecontrol.  
- Created tests for the "extras" like Equinox has.
- Created "appgen" tool that creates everything for CRUDing an object.
- Fixed Spring MVC implementation so getText() method uses the current
  request's locale. 
  https://appfuse.dev.java.net/issues/show_bug.cgi?id=89
- Dependent packages upgraded:
    * Ant Contrib 1.1b1
    * Canoo WebTest build 543
    * Cargo 0.3 (now supports Tomcat 5.5.x)
    * DisplayTag 1.0 RC2 Nightly (to fix unicode export)
    * XDoclet 1.2.2
- Dependent packages added:
    * jMock 1.0.1 - a library for testing Java code using mock objects.
    

Features/Changes in 1.6
============================
- Integrated WebWork as a web framework choice.  To install WebWork (replacing
  Struts) in a fresh AppFuse project, simply run "ant install-webwork".
- Refactored to use SiteMesh instead of Tiles.  Proposal and feedback at: 
    http://raibledesigns.com/page/rd?anchor=should_i_ditch_tiles_in
    Good Article: http://www.onjava.com/pub/a/onjava/2004/09/22/sitemesh.html
    Experience documented at: http://raibledesigns.com/page/rd/20040821
- Re-worked User and Roles relationship to take advantage of Hibernate more.
  Thanks to Daniel Kibler for the patch:
    https://appfuse.dev.java.net/issues/show_bug.cgi?id=69
- Removed UserFormEx and replaced with ability to "merge" methods into an 
  ActionForm using XDoclet.  See metadata/web for xdoclet-UserForm.java
  which now contains methods merged into the generated UserForm.java.
- Modified struts_form.xdt to support nested objects.  Moved address information
  from org.appfuse.model.User to org.appfuse.model.Address to and created
  NestedFormTest to verify it works.  This template will pick up any nested 
  object validations rules.
- Changed "org.appfuse.persistence" package name to "org.appfuse.dao".  Moved
  "*ManagerImpl" classes to "service.impl" package.
- Changed stylesheet colors for Spring MVC option to be green instead of red.
  Bug 47 - https://appfuse.dev.java.net/issues/show_bug.cgi?id=47.
- Refactored logging so Base classes contain a "log" variable that children
  don't need to override. More at:
    http://raibledesigns.com/page/rd?anchor=log_debug_vs_logger_debug
- Refactored toString(), equals() and hashCode() methods in BaseObject to be 
  abstract so child classes have to implement custom methods. Bug at: 
    https://appfuse.dev.java.net/issues/show_bug.cgi?id=65.
  Commonclipse (http://commonclipse.sf.net) can generate the methods for you.
  	Learn more at: http://www.leegrey.com/hmm/2004/09/29/1096491256000.html
- Added Cargo to simplify starting and stopping Tomcat before running Canoo 
  WebTests.  Unfortunately, the 0.2 release doesn't work with Tomcat 5.5.x.
- Refactored all web frameworks to allow for testing out-of-container.  This
  means that "test-web" will now work w/o Cactus or the container running.
    Learn more at: http://raibledesigns.com/page/rd?anchor=ann_cargo_0_2_released
- Removed MainMenuTest, which was a demonstration of how to write tests using
  HttpUnit.  This test caused more problems than it solved.
- Changed target names in build.xml: define-tasks -> init, init -> prepare. 
  Reworked build.xml so XDoclet tasks don't execute when they don't need to.
- Added translations for French and Spanish.
- Added PasswordHintTest for those controllers and actions that implement this
  feature.
- Added SimpleMappingExceptionResolver for Spring version to catch 
  DataAccessExceptions and forward the user to dataAccessFailure.jsp.  Added
  this same functionality to the WebWork version.
- Added message to the Reload Action so users will see a message when reloading
  options.  Message is currently hard-coded to English in Actions/Controllers.
- Removed proprietary State and Country Tags.  Implemented custom CountryTag
  (thanks Jens Fischer) and made state into a text box since not all countries 
  have "states".
- Reworked mail support to use Spring's MailSender and changed account 
  information e-mail (for Spring and WebWork options) to use a Velocity template.  
  This was partially motivated by all the questions I get on Sending 
  Velocity-based E-Mail with Spring.
  http://jroller.com/page/raible/20040406#sending_velocity_based_e_mail
- Consolidated all mail settings to mail.properties (in web/WEB-INF/classes).
- Added support for detecting and configuring Tomcat 5.5.x. 
    http://tinyurl.com/5ebdh
- Renamed "Secure" tag library to "SecureTag" for consistency.
- Added field-level errors (using html:errors) to Struts JSPs and viewgen for 
  Struts.
- Changed column names in User object from camelCase to normal database_names.
- Added fallback locale of 'en' in metadata/web/web-settings.xml in case no
  bundle is found for the browser's preferred locale.
- Added native2ascii task in copy-resources to encoded to ascii with unicode 
  escapes. 
- Added Dumbster (http://quintanasoft.com/dumbster/) to catch and verify 
  e-mail messages sent during test execution.
- Changed parameter key for indicating methods in Struts Actions from "action" 
  to "method" in order to alleviate conflicts with the "action" attribute
  in an HTML form.
- Changed name of generated WAR file to *not* include the version number. 
  Having the version number as part of the name seemed to cause more problems
  than it solved.
- Added forkmode="true" to junit task in "test-module" target - greatly 
  increasing the speed of test execution - particularly on my PowerBook.
    http://raibledesigns.com/page/rd?anchor=aren_t_out_of_container
- Dependent packages upgraded:
    * DbUnit 2.1
    * Display Tag 1.0 RC1
    * Hibernate 2.1.6
    * iBATIS 2.0.7
    * JSTL 1.0.6
    * MySQL JDBC Driver 3.0.14
    * Spring 1.1.1
    * Struts 1.2.4
    * Struts Menu 2.3
    * Struts Test Case 2.1.2
    * WebTest build474
    * XDoclet 1.2.2 RC1
      ** WARNING: Running "ant setup test-all" will result in the error:
                  "destDir attribute must be present." Running "ant setup" 
                  and then "ant test-all" is an easy workaround.
                  http://opensource.atlassian.com/projects/xdoclet/browse/XDT-879                  
- Dependent packages added:
    * Cargo 0.2 - A set of Ant tasks for starting and stopping Java containers.
    * Dumbster 1.2 - A fake SMTP server used to catch and verify messages in
      unit tests.
    * SiteMesh 2.2 - A page-decoration package that will work across different 
      web frameworks.
    * URL Rewrite Filter 1.2 - Added and included in WAR, but disabled by
      default.  See web/WEB-INF/urlrewrite.xml for more information.
- Dependent packages removed:
	* State Tag and Country Tag - required a $75 license for production use.
	* Cactus - no longer needed as all tests can be run out-of-container and 
	  Cargo can be used to start Tomcat for in-container (JSP) tests.


Features/Changes in 1.5
============================
- Added Spring MVC as a web framework option. You can install it by navigating
  to "extras/spring" and typing "ant install".  Includes Commons Validator
  and XDoclet support for generating validation.xml.  I also ported the 
  LabelTag so it works with Spring as well.  Make sure and read the README.txt
  in extras/spring for issues I encountered while developing.
- Changed extension for default Controller to be *.html.  We're serving up
  HTML, so it makes sense (to me) to use this instead of .do?  I'm also 
  motivated because I want to be more MVC framework-agnostic.
- Removed Struts dependency from "services" layer.  Actions can use a convert()
  method to transfer POJOs to Forms and vise versa.
- Fixed i18n (thanks Jaap!) - now reads from the user's browser's settings.
  Available languages are English, Dutch (Jaap van der Molen), Brazilian 
  (Gilberto Caetano de Andrade) and Chinese (Paul Wang).
- Fixed bug where logout didn't work when Remember Me was disabled (issue #3).
- Fixed bug in struts_form.xdt where invalid code generated for ObjectFactory
  inner class (issue #2).  Also added "indexedProperties" attribute to 
  @struts.form tag.  This is so indexed property support is only generated when 
  the user wants it to be.  Indexed property support is NOT generated by 
  default. Here is an example: 
    @struts.form include-all="true" extends="BaseForm" indexedProperties="true"
  - More information at: 
    http://raibledesigns.com/wiki/Wiki.jsp?page=XDocletIndexedProperties
- Modified XDoclet to create a standalone "actionform" task that generates
  ActionForms from POJOs.  See the "compile-web" target for more information.
- Added activation.jar, mail.jar and jta.jar to "tomcat-setup" target for 
  easier setup of Tomcat.
- Fixed deploy-web task so invalid test-only files are not copied to webapps
  directory.
- Added CurrencyConverter and DateConverter to BaseManager and ConvertUtils
  initialization.  Added currency mask to validation-global.xml. Also
  changed default Long to be null instead of zero (0).
- Added Editable Table example (at http://localhost:8080/appfuse/users.jsp) 
  using the displaytag and JSTL's SQL Tags.
- Added Dynamic (database-driven) menu example using Struts Menu (at
  http://localhost:8080/appfuse/dynamicMenu.jsp).
- Added BaseDAOHibernate with convenience methods: getObject(), saveObject,
  removeObject().
- Added documentation on common Ant targets I use:
  http://raibledesigns.com/wiki/Wiki.jsp?page=AppFuseAntTasks
- Added ReloadAction to reload drop-downs initialized in StartupListener. 
  Also added "Reload Options" menu item under "Administration" to invoke 
  this action.
- Changed Tomcat 5 detection to use a class from 5.0.x rather than parsing
  the folder name of the installed server.
- Added encoding options for UTF-8 so AppFuse can handle languages such as
  Chinese. (Thanks Paul Wang)
- Moved StrutsGen Tool and iBatis install to a common "extras" folder.
- Renamed StrutsGen Tool to JSPGen so it can be used to generate default
  master/detail screens for other frameworks.
- Changed Struts Menu to use MenuContextListener for initialization since its
  framework-agnostic.
- Dependent packages upgraded:
    * Hibernate 2.1.3
    * WebTest build 432
    * XDoclet 1.2.1-dev (generation of validation.xml for Spring and ActionForm
      generation that doesn't require j2ee.jar in classpath)
    * Velocity 1.4
- Dependent packages added:
    * Request Taglib 1.0.1 (can be used instead of "logic:present role='...'" 
      since it can be used across JSP-based MVC frameworks)
        ** Not used in Struts MVC, *is* used in Spring MVC
        ** Needs to be modified to support a comma-delimited list of roles.


Features/Changes in 1.4
=======================
- Added "cactus" task for running Cactus tests in Tomcat 4.x and Resin 3.0.5.
- Added Tomcat Ant tasks for managing tomcat with the Manager app and Ant.
  - Currently supports: install, remove, start, stop, reload and list.
  - I recommend using "ant setup-tomcat deploy" and then using "ant reload"
    after running "ant deploy" when you change .java or config files.
- Removed "upload" module and integrated file-upload into the core. Removed 
  unnecessary scriplets from uploadForm.jsp.
- UI Enhancements: 
  - Changed CSS for error messages to have a red border around them - making
    it easier to distinguish errors from messages.
  - Added onclick and onfocus event handler to form inputs to select the 
    text when an input type="text" is clicked or focuses on.
- Changed directory structure from common/ejb/web to dao/service/web. 
  More information on this change at:
  http://raibledesigns.com/page/rd?anchor=appfuse_refactorings_part_i_changing
- Added Spring to configure Hibernate, DAOs and Managers.  Configured 
  declarative transactions at the service and dao layers.  Configured 
  OpenSessionInViewFilter for guaranteeing one transaction per request.
  More information on this change at:
  http://raibledesigns.com/page/rd?anchor=appfuse_refactorings_part_ii_spring
- Changed UserCounterListener to only record users that have logged in 
  successfully.  Also added a screen to show currently logged in users.
- Changed default session timeout to 10 minutes instead of 60.
- Implemented persistent login cookie strategy (for Remember Me) as 
  recommended by Charles: http://tinyurl.com/2wyqr
  More information on this change at:
  http://raibledesigns.com/page/rd?anchor=appfuse_refactorings_part_iii_remember
- Added iBATIS as a persistence framework option.  More information on this
  feature can be found at:
  http://raibledesigns.com/page/rd?anchor=appfuse_refactorings_part_iv_replacing
- Added custom web.xml XDoclet template for ordering filters and listeners.
- Added support for generating indexed property setters in ActionForms when
  generating Forms with XDoclet.  This support includes adding Velocity JARs
  to the the list of 3rd party JARs.  Currently, Velocity is only used by 
  XDoclet.
- Added "Account Information" e-mail as part of registration process.  This
  e-mail gets sent the e-mail address the user entered on signup.
- Dependent packages upgraded:
    * Cactus 1.6 Nightly (20030119) to support the "cactus" task and Resin 
      3.0.5
    * JSTL 1.0.5
    * Patched Canoo's WebTest to work with Ant 1.6
    * Hibernate 2.1.2
    * MySQL JDBC Driver 3.0.11


Features/Changes in 1.3
=======================
- Many changes to database settings so that database.properties is generated
  at run-time from properties in build.properties (defaults to MySQL).  This
  makes it easy for users to run a MySQL database in development and have a
  DB2 database (or any other) in production.  Just customize your database 
  settings and put them in ~/.build.properties or ~/.appname- build.properties
  and these settings will be used instead of the default.
  - As part of this process, I tested AppFuse on DB2 8.1 (on Windows) 
    and PostgreSQL 7.4 (on OS X).  
  - Testing on other servers caused me to change from generator-class="native"
    to generator-class="increment" - which still works fine on MySQL.
  - I also changed tomcat-context.xml to dynamically substitute database
    properties and defaultAutoCommit is now "true" by default.
- Added error pages for 404 (page not found) and 403 (access denied), both 
  with pretty pictures. ;0) Protected /editUser.do for admin role only.
- Moved filter-mappings from Filter's JavaDocs (XDoclet) to 
  metadata/web/filter-mappings.xml in order to control the order of execution.
- Made RememberMe feature configurable via a "rememberMe.enabled" property in
  app-settings.xml. This won't kick on resin until the filter is invoked at
  least once.  Tomcat initializes filters on startup.
- Upgraded oscache.jar in Hibernate 2.1.1 to OSCache 2.0.1 and configured
  OSCache to cache JSP changes.  Also modified the oscache-2.0.1.jar to have
  a URI for the tag library.  Turned off caching of JSP pages - to enable, 
  uncomment filter-mapping in metadata/web/filter-mappings.xml.
- Made changes to be Resin 3.0.4 friendly.  More info at:
  http://raibledesigns.com/wiki/Wiki.jsp?page=AppFuseOnResin
- Refactored BaseDAOHibernate to consolidate retrieveObject and removeObject
  methods.  Also changed saveObject to do ses.saveOrUpdate and removed 
  storeObject method. 
- Replaced CompressionFilter with GZipFilter that works on Resin.
  http://www.onjava.com/pub/a/onjava/2003/11/19/filters.html
- Added print stylesheet support.
- Added Clickstream (http://www.opensymphony.com/clickstream) and menu/JSPs to
  view user paths.
- Dependent packages upgraded:
    * XDoclet 1.2.0
    * Cactus 1.6 Nightly (20030116) to support "cactus" task for supporting
      a "contextxml" attribute for testing in Tomcat.
  

Features/Changes in 1.2
=======================
- Backed out Http Post for Remember Me.  It was not redirecting user to the page
  they originally requested.  Using reponse.sendRedirect does send the user
  to the proper location.  Turned on password encryption (SHA) to protect any
  passwords that end up in log files.  Turned off encryption in Tomcat.
- Changed configuration parameters in servlet context to be in a hashmap.
- Improvements to StrutsGen tool to generate list screen as well and to fill
  in more missing elements.
- Changed to close Hibernate session when object not found in BaseDAOHibernate.
- Dependent packages upgraded:
    * Hibernate 2.1.1
    * Struts Menu 2.1
    * WebTest Build 379
- Fixed bug in UserAction.save: when creating a new user, role defaults to 
  "tomcat" regardless of what the user chooses.
  
  
Features/Changes in 1.1
=======================
- Documentation!  Now includes tutorials (in docs/index.html) for doing a full
  master-detail lifecycle (database to JSP).  HowTos are pulled from my wiki
  (http://raibledesigns.com/wiki) using "ant wiki".  Please update the wiki
  if you find any errors in the documentation.
- Now requires J2EE 1.4 - if you're not there yet, simply change the paths
  for activation.jar and mail.jar in properties.xml (look for 
  common.compile.classpath). You can use j2ee.jar instead of mail.jar and
  activation.jar for 1.3 and 1.4 B2.
- Dependent packages upgraded:
    * Hibernate 2.1
    * Struts Nightly Build 20031202
    * Display Tag Library 1.0 Beta 2
- Fixed bugs in build.xml "new" target to copy Eclipse files (.project and
  .classpath) into new project.
- Fixed issues in error.jsp, ActionExceptionHandler, UserDAOHibernate and 
  RegistrationServlet where exceptions weren't being reported accurately.
- Modified template for creating JSPs from ActionForms to more closely match
  current design.
- Renamed targets in test/web-tests.xml to be CamelCase.  I changed these 
  because when you're typing -Dtestcase=Name, I've found that I'm used to 
  doing CamelCase for my JUnit Tests.
- Fixed bug on signup.jsp where State and Country didn't retain values
  when an error occurred.
- Removed "copy-jars" target in build.xml - moved the process of including 
  jars into the war task of the package-web target.
- Fixed "setup-tomcat" target to detect Tomcat 5 and deploy appfuse.xml
  to $CATALINA_HOME/conf/Catalina/localhost instead of $CATALINA_HOME/webapps.
- Changed "test-common" task to work with J2EE 1.4 Final.  This involved
  removing ${j2ee.jar} from the classpath and adding mail.jar and 
  activation.jar explicitly.  You can change this in properties.xml.
  
  
Features/Changes in 1.0
=======================
- Dependent packages upgraded: 
    * Cactus 1.5
    * DBUnit 1.5.6
    * Hibernate 2.0.3
    * Struts Menu 2.0
    * JSTL 1.0.4
    * MySQL Driver 3.0.9
- Refactored "Remember Me" to be more secure by setting cookies only under
  the "/security/*" path and only retrieving them from there.
    - Renamed BreadCrumbFilter to LoginFilter and removed Breadcrumb 
      functionality (wasn't used anyway).
- Improved security of "Remember Me" to do an HTTP POST (instead of a GET)
  using commons HttpClient.  This way usernames and password will not show
  up in the user's browser address bar, their browser's history, or server
  log files.
- Removed Hibernate's Session from DAO and Manager method signatures - now
  it an object is passed into the implementation constructors.
- Refactored DAOFactory to return DAOs based on types of objects in the
  constructors (Bear Giles).
    - Example from LookupDAOTest.java:
        dao = (LookupDAO) DAOFactory.getInstance(conn, LookupDAO.class);
- Decoupled Manager interfaces from Struts - now only objects are passed, then
  cast to ActionForms in the ManagerImpl classes.
    - Currently there is no factory for creating Managers, should there be one?
    - Possibly use Spring to give ManagerImpl's to Actions.
- Refactored Actions to pass Objects for Open-Session-In-View Pattern and
  removed daoType variable from Manager's contructors and it's clients (Actions
  and Tests).
- Upgraded to nightly build (November 11th) of Struts.  Details available at:
  http://raibledesigns.com/page/rd?anchor=upgrading_from_struts_1_1
- Added BaseManagerTestCase and BaseDAOTestCase for common methods and 
  populating objects (i.e. Forms and POJOs) from .properties files.  If you
  put a .properties file in the same directory as a *Test.java, it will
  be loaded and available as a ResourceBundle - assigned to variable "rb".
    - Example from UserManagerTest.java:
        userForm = new UserForm();
        // call populate method in super class to populate test data
        // from a properties file matching this class name
        userForm = (UserForm) populate(userForm);
- Removed JUnitDoclet comments from existing tests.  I did this because
  I found they were confusing when trying to explain JUnit and the testcases
  to peers.
- Removed JUnit TestSuites - not needed since junit task and batchtest handle 
  this.
- Added DateUtil.java and DateUtilTest.java, as well as calendar.js for popup
  calendars (from http://www.mattkruse.com/javascript/calendarpopup/).
- Added enhancements to error handling and logging in ActionExceptionHandler
  and error.jsp.  Details available at: 
  http://jroller.com/page/prpatel?anchor=handling_the_three_kinds_of
- Changed User Profile to retain password since it's encrypted anyway.
- UNTESTED: Removed mysql values from being hardcoded in build.xml. Should work 
  with PostgreSQL by changing mysql properties to postgresql properties in 
  properties.xml.  
    - Look for "database.jar" to begin changing to postgresql - 
      there are commented out versions for postgresql.  
    - You will also need to change metadata/web/tomcat-context.xml for 
      PostgreSQL.
    - You will need to download the database driver for postgresql and put it
      in the lib directory and adjust lib.properties appropriately.
- Added auto-generation of "reset" method (for booleans) in generated 
  ActionForms - accomplished by modifying struts_form.xdt.
- Removed Parent and Child Objects and any accompanying sample data. Get the
  tag 0_9_9 if you want to play with them.
- Added SwitchLayoutAction (not used) - more details available at:
  http://raibledesigns.com/page/rd?anchor=tiles_tips_o_the_day
- Added compile-jsp.xml for pre-compiling JSPs in Tomcat. You can run it using
  "ant -f compile-jsp.xml".

###
