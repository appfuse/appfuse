package org.appfuse.webapp.action;

import java.io.IOException;

import org.apache.tapestry.IRequestCycle;
import org.appfuse.webapp.listener.StartupListener;

public class Reload extends BasePage {

    public void execute(IRequestCycle cycle) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'execute' method");
        }

        StartupListener.setupContext(getServletContext());
        MainMenu nextPage = (MainMenu) cycle.getPage("mainMenu");
        nextPage.setMessage(getMessage("reload.succeeded"));            
        cycle.activate(nextPage);
    }
}
