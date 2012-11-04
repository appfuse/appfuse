package org.appfuse.webapp.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * AppFuse meta component; used in the header
 *
 * @author Serge Eby
 */
public class Meta {

    @Property
    @Inject
    @Path("context:images/favicon.ico")
    private Asset favoriteIcon;
}
