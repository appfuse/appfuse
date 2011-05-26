package org.appfuse.webapp.pages;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.appfuse.webapp.util.MessageUtil;

/**
 * Page to handle 403 errors 
 * 
 * @author Serge Eby
 * @version $Id: AccessDenied.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class AccessDenied {

    @Property
    @Inject
    @Path("context:images/403.jpg")
    private Asset accessDeniedImage;

    @Inject
    private Messages messages;

    @Inject
    private PageRenderLinkSource linker;

    public String getaccessDeniedMessage() {
        String message = MessageUtil.convert(messages.get("403.message"));
        String url = linker.createPageRenderLink("Index").toURI();
        return String.format(message, url);
    }
}
