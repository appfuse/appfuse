package org.appfuse.webapp.action;

import org.appfuse.webapp.listener.StartupListener;

import java.io.IOException;
import java.io.Serializable;

/**
 * JSF Page class to handle reloading options in servlet context.
 *
 * @author Matt Raible
 */
public class Reload extends BasePage implements Serializable {
    private static final long serialVersionUID = 2466200890766730676L;

    public String execute() throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("Entering 'execute' method");
        }

        StartupListener.setupContext(getServletContext());
        addMessage("reload.succeeded"); 

        return "success";
    }

}
