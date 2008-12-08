package org.appfuse.webapp.pages.admin;

import com.opensymphony.oscache.web.ServletCacheAdministrator;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.slf4j.Logger;

/**
 * Flush cache
 *
 * @author Serge Eby
 * @version $Id: FlushCache.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class FlushCache {

    @Inject
    private Logger logger;

    @Property
    @Inject
    @Path("context:images/iconInformation.gif")
    private Asset iconInformation;


    @Inject
    private ApplicationGlobals globals;

    private ServletCacheAdministrator admin;

    void onActivate() {
        if (admin == null) {
            admin = ServletCacheAdministrator.getInstance(globals.getServletContext());
        }
    }

    void beginRender() {
        logger.debug("Ready to flush the cache");
        admin.flushAll();
    }

}
