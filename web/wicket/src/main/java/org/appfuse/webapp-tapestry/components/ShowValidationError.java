package org.appfuse.webapp.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;

/**
 * Displays validation error message
 *
 * @author Serge Eby
 * @version $Id: ShowValidationError.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ShowValidationError {

    @Inject
    @Path("context:/images/iconWarning.gif")
    private Asset icon;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;

    @Property
    @Parameter(required = true, allowNull = true)
    private String error;


    Binding defaultError() {
        return defaultProvider.defaultBinding("error", resources);
    }

    public String getError() {
        return error;
    }

    final boolean beginRender(MarkupWriter writer) {
        // Skip if no empty of null message
        if (error == null || "".equals(error)) {
            return false;
        }

        writer.element("div",
                "class", "error");
        writer.element("img",
                "src", icon.toClientURL(),
                "alt", messages.get("icon.warning"),
                "class", "icon");
        writer.write(error);
        writer.end(); //img 

        writer.end(); // div

        return false;
    }

}
