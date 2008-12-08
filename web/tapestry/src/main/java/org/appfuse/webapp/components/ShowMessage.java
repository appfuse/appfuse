package org.appfuse.webapp.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;

/**
 * Displays informational message
 *
 * @author Serge Eby
 * @version $Id: ShowMessage.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ShowMessage {

    @Property
    @Inject
    @Path("context:/images/iconInformation.gif")
    private Asset icon;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;

    @Property
    @Parameter(required = true, allowNull = true)
    private String message;

    Binding defaultMessage() {
        return defaultProvider.defaultBinding("message", resources);
    }

    final boolean beginRender(MarkupWriter writer) {
        // Skip if empty or null
        if (message == null || "".equals(message)) {
            return false;
        }

        writer.element("div",
                "class", "message");
        writer.element("img",
                "src", icon.toClientURL(),
                "alt", messages.get("icon.information"),
                "class", "icon");
        writer.write(message);
        //writer.writeRaw(message);
        writer.end(); // img

        writer.end(); // div

        return false;
    }
}
