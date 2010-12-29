package org.appfuse.webapp.pages.admin;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.appfuse.webapp.listener.StartupListener;
import org.appfuse.webapp.pages.BasePage;
import org.appfuse.webapp.pages.MainMenu;

import java.io.IOException;

/**
 * @author Serge Eby
 * @version $Id: Reload.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class Reload extends BasePage {

    @InjectPage
    private MainMenu mainMenu;

    @Inject
    private ApplicationGlobals globals;

    Object onActivate() throws IOException {
        StartupListener.setupContext(globals.getServletContext());
        mainMenu.addInfo("reload.succeeded", true);
        return mainMenu;
    }
}
