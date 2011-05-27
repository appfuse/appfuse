package org.appfuse.webapp;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

/**
 * Abstract WebPage for every page in a project.
 *
 * @author Marcin Zajączkowski, 2011-02-07
 */
public abstract class AbstractWebPage extends WebPage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected ServletContext getServletContext() {
        return ((WebApplication)getApplication()).getServletContext();
    }
}
