package org.appfuse.webapp.pages.admin;

import java.io.IOException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.appfuse.webapp.listener.StartupListener;
import org.appfuse.webapp.pages.BasePage;
import org.appfuse.webapp.pages.MainMenu;

public abstract class Reload extends BasePage {
    public abstract IEngineService getEngineService();

    public ILink execute(IRequestCycle cycle) throws IOException {
        log.debug("Entering 'execute' method");

        StartupListener.setupContext(getServletContext());
        MainMenu nextPage = (MainMenu) cycle.getPage("mainMenu");
        nextPage.setMessage(getText("reload.succeeded"));            
        return getEngineService().getLink(false, nextPage.getPageName());
    }
}
