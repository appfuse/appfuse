package org.appfuse.webapp;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
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
 *  - add using i18n resources from standard AppFuse directory
 *  - reusable component for editing user data - with subclassing for Sing up and others? - DONE
 *  - roles support during editing user (UserEdit) - DONE
 *  - full support for save and delete in diffrent modes - WIP (what with password?)
 *  - activeUsers? - LATER
 *  - add integration with j_security_check - action in a wicket form cannot be used - Wicket overrides it -
 * workaround with redirect?
 *  - RememberMe feature from Spring security - does it work?
 *  - removing rememberMe cookie during Logout - DONE
 *  - file upload - looks strange to upload files to application directory - LATER
 *  - change mouse cursor over link in a table (UserList)
 *  - RequiredLabel enhancements (label with text component?)
 *  - mainMenu -> home - LATER - requires changes in web-common
 *  - the same buttons twice (UserEdit) - DONE
 *  - some tests
 *  - add "Are you sure?" question on delete user (JS alert?) - DONE
 *  - Clickstream - LATER
 *  - move pages to resources directory (currently together with Java classes),
 *    try: https://cwiki.apache.org/WICKET/control-where-html-files-are-loaded-from.html#ControlwhereHTMLfilesareloadedfrom-InWicket1.4
 *  - on "mvn clean package" WicketApplication.properties isn't copied to target which causes:
 *    'Unable to find property: 'user.password' for component: userEditForm:userEditPanel' in tests. When run from IDE
 *    file is copied and tests from Maven works fine
 *  - broken acceptance test: web/wicket/src/test/resources/login.xmlf:1: HTTP error 400: 400 Bad Request for http://localhost:9876/scripts/login.js
 *  - assign roles doesn't work when editing an user from a list - #14 - DONE
 *
 * @author Marcin Zajączkowski, 2010-09-02
 */
public class WicketApplication extends AuthenticatedWebApplication {

    private static final String BASE_PACKAGE_FOR_PAGES = "org.appfuse.webapp.pages";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void init() {
        super.init();

        addComponentInstantiationListener(new SpringComponentInjector(this, getContext(), true));
        initPageMounting();

        //MZA: Redirect after post causes page to be shrunk (probably) due to SiteMesh bug:
        //http://jira.opensymphony.com/browse/SIM-217
        getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.ONE_PASS_RENDER);

        //TODO: MZA: Add app.properties
/*
        String configurationType = getConfigurationType();
        if (DEVELOPMENT.equalsIgnoreCase(configurationType)) {
            System.out.println("devel");
//          log.info("You are in DEVELOPMENT mode");
          getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
        }
*/
    }

    private void initPageMounting() {
        //Hint from:
        //http://blog.xebia.com/2008/10/09/readable-url%E2%80%99s-in-wicket-an-introduction-to-wicketstuff-annotation/
        new AnnotatedMountScanner().scanPackage(BASE_PACKAGE_FOR_PAGES).mount(this);
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
