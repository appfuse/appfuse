package org.appfuse.webapp.pages;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.appfuse.webapp.util.MessageUtil;

/**
 * Page to be displayed whenever a page is not found (404 error)
 *
 * @author Serge Eby
 * @version $Id: NotFound.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class NotFound {

    @Property
    @Inject
    @Path("context:images/404.jpg")
    private Asset notFoundImage;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources resources;

    public String getNotFoundMessage() {
        String message = MessageUtil.convert(messages.get("404.message"));
        String url = resources.createPageLink("MainMenu", false).toURI();
        return String.format(message, url);
    }


}
