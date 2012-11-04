package org.appfuse.webapp.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import java.util.List;

/**
 * Main index page. This also handles 404 errors
 *
 * @author Serge Eby
 * @version $Id: Index.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class Index {

    @Inject
    private Logger logger;

    @Inject
    private RequestGlobals globals;

    @Inject
    private Request request;

    @Inject
    private Response response;


    @SuppressWarnings("unchecked")
    private List eventContext;

    @SuppressWarnings("unchecked")
    public List getEventContext() {
        return eventContext;
    }

    Object onActivate(List context) {
        eventContext = context;
        if (context != null && !context.isEmpty()) {
            int index = 0;
            for (Object obj : context) {
                index++;
                logger.debug(String.format("Context #%d =  %s", index, obj.toString()));
            }
            logger.debug("Redirecting to PageNotFound");
            return NotFound.class;
        }
        // Redirect to Home
        return Home.class;

    }
}
