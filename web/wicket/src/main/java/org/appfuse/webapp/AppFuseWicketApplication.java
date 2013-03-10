package org.appfuse.webapp;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.appfuse.webapp.pages.Login;
import org.apache.wicket.Page;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.appfuse.webapp.pages.MainMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

/**
 * AppFuse Wicket Frontend Application class.
 *
 * Main TODO:
 *  - getting data of current user (UserEdit) - DONE
 *  - sorting efficiency (UserList)
 *  - add using i18n resources from standard AppFuse directory - a workaround in pom.xml - a custom ResourceLoader
 * using app*.properties should be written
 *  - reusable component for editing user data - with subclassing for Sing up and others? - DONE
 *  - roles support during editing user (UserEdit) - DONE
 *  - full support for save and delete in different modes - WIP (what with password?)
 *  - activeUsers? - DONE
 *  - add integration with j_security_check - action in a wicket form cannot be used - Wicket overrides it -
 * workaround with redirect?
 *  - RememberMe feature from Spring security - does it work?
 *  - removing rememberMe cookie during Logout - DONE
 *  - file upload - looks strange to upload files to application directory - LATER
 *  - change mouse cursor over link in a table (UserList)
 *  - RequiredLabel enhancements (label with text component?) - OBSOLETE
 *  - mainMenu -> home - LATER - requires changes in web-common
 *  - the same buttons twice (UserEdit) - DONE
 *  - some tests - DONE
 *  - add "Are you sure?" question on delete user (JS alert?) - DONE
 *  - Clickstream - LATER
 *  - move pages to resources directory (currently together with Java classes),
 *    try: https://cwiki.apache.org/WICKET/control-where-html-files-are-loaded-from.html#ControlwhereHTMLfilesareloadedfrom-InWicket1.4
 *  - on "mvn clean package" fooWicketApplication.properties isn't copied to target which causes:
 *    'Unable to find property: 'user.password' for component: userEditForm:userEditPanel' in tests. When run from IDE
 *    file is copied and tests from Maven works fine - DONE (added resources from /src/main/java/ in pom.xml)
 *  - broken acceptance tests (part 1): web/wicket/src/test/resources/login.xmlf:1: HTTP error 400: 400 Bad Request for http://localhost:9876/scripts/login.js - DONE
 *  - broken acceptance tests (part 2): 400 Bad Request for http://localhost:9876/appfuse-wicket-2.1.0-SNAPSHOT/../../login
 *    on password hint (web-tests.xml:52) - see comment PasswordHint class
 *  - broken acceptance tests (part 3) - Sign up page has wrong title - AbstractUserEdit.html markup title should be change depending on
 *    a concrete page (Sign up, Edit user, ...) and other broken tests
 *  - decide if there should be page url passwordHint or passwordhint
 *  - assign roles doesn't work when editing an user from a list - #14 - DONE
 *  - Java scripts on a Login page doesn't work. JS error: "ReferenceError: $ is not defined" in global.js could be a reason - DONE -
 *    Tapestry had its own prototype.js import and it was removed also in Wicket default.jsp
 *  - find some better way to create parametrized string messages than StringResourceModel
 *  - "checkbox column" in a table - PhoneBook sample application has that one
 *
 * Migration to 1.5
 *  - password hint url is not properly generated - http://localhost:8080/login? - possible problem with injecting JavaScript - DONE
 *  - check if absolute url is properly created (3 places) - DONE
 *
 * Migration to 6
 *  - DataTable style has changed and the header takes two lines instead of one - LATER (when with Bootstrap) - DONE
 *  - JavaScript on a login page doesn't work - a lot of error messages - probably Prototype conflicts with JQuery - DONE
 *
 * Files copied from web-common to make some Wicket specific changes (should be unified):
 *  - default.jsp
 *  - style.css (changes in common)
 *  - login.js
 *
 * Integration with Bootstrap and Wicket 2.2
 *  - remove (probably) not needed references to prototype.js and friends - DONE
 *  - add Required and Placeholder behavior - http://tom.hombergs.de/2011/12/wicket-html5-required-and-placeholder.html - DONE
 *  - move all required common JS/CSS imports from default.jsp to base WebPage
 *  - why #login p from style doesn't work on a login page? - DONE
 *  - make 'decorator:getProperty property="body.class"' works - DONE
 *  - maybe it is worth to get rid of default.jsp and use some template mechanism from Wicket?
 *  - fix problem with remaining red div after dismiss an error message
 *  - adjust style.css to Wicket styles for table header - DONE
 *  - take a look on some nice looking classes available in wicket-bootstrap library
 *  - why empty wicket:message from <title> is rendered inside body (and by the way breaks layout on "Current User" page) - OBSOLETE
 *  - sync localized messages with upstream - DONE
 *  - icons on button - <a><i class="icon-plus icon-white"></i> Add</a> - DONE
 *  - collapsible address section - DONE
 *  - input inside label for "Account Settings" - a new component?
 *  - add placeholder and required behavior to TextField on UserEditPanel (a new component?) - DONE
 *  (just added required behavior, placeholder is not used there)
 *  - Cancel button doesn't work with HTML5 required attribute (remove it and use back button in a browser?) - DONE - Link used instead of Button
 *  - setRequired(true) is not compatible with NotificationPanel - DONE (fixed upstream)
 *  - fix acceptance tests: 404 Not Found for http://localhost:8888/AppFuse - login.xmlf:1 - DONE (old config.xmlf)
 *  - fix acceptance test: setselectfield - country - DONE - workaround with optionIndex
 *  - change button name on Signup page: Save -> Signup
 *  - change page title on Signup page: User Settings -> Signup - DONE
 *  - fix acceptance test: signup - doesn't move to a login page and others - currently disabled in Maven
 *  - resolve problem with sending password hash to an user on edit - APF-1370
 *  - arrows in a table should be next to a header label (not at the end of a column on the right side)
 *
 *  - rename AppFuseWicketApplication to AppFuseWicketApplication - DONE
 *  - Archetype: missing scripts/login.js - DONE - moved it to webapp/scripts/
 *  - Archetype: html files are pages directory in addition to webapp/pages - DONE - an ugly hack required due to APF-1378
 *  - Archetype: remove bootstrap and jquery related files (webapp/styles/lib/*) - DONE
 *  - are common/*.jsp, appfuse.tld, EscapeXml*.java, taglib/* really needed
 *  - UserCounterListener could be used instead of Spring Security to count and display active users
 *  - Archetype: remove src/test/resources/target and remove src/test/java-tapestry - DONE
 *  - Archetype: src/main/webapp/scripts cannot be deleted - it removes includes *\/*.html and *\/*.tld for src/main/webapp in maven-archetype-metadata.xml
 *
 * @author Marcin Zajączkowski, 2010-09-02
 */
public class AppFuseWicketApplication extends AuthenticatedWebApplication {

    private static final String BASE_PACKAGE_FOR_PAGES = "org.appfuse.webapp.pages";
    public static final String APP_FUSE_RESOURCE_FILE_NAME = "ApplicationResources";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void init() {
        super.init();
        registerSpringComponentInjector();
        initPageMounting();
        setOnePassRenderStrategy();
        registerAppFuseSpecificStringResourceLoader();
        initBootstrap();
    }

    private void registerSpringComponentInjector() {
        getComponentInstantiationListeners().add(new SpringComponentInjector(this, getContext(), true));
    }

    private void initPageMounting() {
        //Hint from:
        //http://blog.xebia.com/2008/10/09/readable-url%E2%80%99s-in-wicket-an-introduction-to-wicketstuff-annotation/
        new AnnotatedMountScanner().scanPackage(BASE_PACKAGE_FOR_PAGES).mount(this);
    }

    /**
     * Redirects after post causes page to be shrunk (probably) due to SiteMesh bug:
     * http://jira.opensymphony.com/browse/SIM-217
     */
    private void setOnePassRenderStrategy() {
        getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
    }

    /**
     * Registers AppFuse specific IStringResourceLoader at the end to use ApplicationResources.properties
     * for unhandled keys.
     */
    private void registerAppFuseSpecificStringResourceLoader() {
        Class applicationResourcesClass = getOrCreateArtificialApplicationResourcesClass();
        getResourceSettings().getStringResourceLoaders().add(new ClassStringResourceLoader(applicationResourcesClass));
    }

    private Class<?> getOrCreateArtificialApplicationResourcesClass() {
        try {
            return Class.forName(APP_FUSE_RESOURCE_FILE_NAME);
        } catch (ClassNotFoundException e) {
            return createArtificialApplicationResourcesClass();
        }
    }

    private Class<?> createArtificialApplicationResourcesClass() {
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass applicationResourcesCtClass = classPool.makeClass(APP_FUSE_RESOURCE_FILE_NAME);
            return applicationResourcesCtClass.toClass();
        } catch (CannotCompileException e) {
            throw new RuntimeException("Unable to instantiate Wicket application", e);
        }
    }

    private void initBootstrap() {
        BootstrapSettings settings = new BootstrapSettings();
        Bootstrap.install(this, settings);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return MainMenu.class;
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return SSAuthenticatedWebSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return Login.class;
    }

    protected ApplicationContext getContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }
}
