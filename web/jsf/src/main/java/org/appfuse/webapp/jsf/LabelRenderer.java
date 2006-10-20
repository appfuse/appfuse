package org.appfuse.webapp.jsf;

import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Custom LabelRenderer component that adds asterisks for required
 * fields.  Based off of an example from David Geary on the MyFaces
 * Mailing list.
 *
 * @author Matt Raible
 */
public class LabelRenderer extends Renderer {
    protected final Log log = LogFactory.getLog(LabelRenderer.class);

    public boolean getRendersChildren() {
        return false;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
    throws java.io.IOException {
        ResponseWriter writer = context.getResponseWriter();

        Map attrs = component.getAttributes();
        String id = (String) attrs.get("for");

        UIInput input = null;
        
        if (!StringUtils.isEmpty(id)) {
            input = (UIInput) component.findComponent(id);
        }

        writer.startElement("label", component);

        String styleClass = (String) component.getAttributes().get("styleClass");

        boolean hasErrors = hasMessages(context, input);

        if (styleClass != null) {
            if (hasErrors) styleClass += " error";
            writer.writeAttribute("class", styleClass, null);
        } else if (hasErrors) {
            writer.writeAttribute("class", "error", null);
        }

        String renderedId =
            (input != null) ? input.getClientId(context)
                            : component.getClientId(context);
        writer.writeAttribute("for", renderedId, null);
        writer.write(attrs.get("value").toString());
    }

    public void encodeEnd(FacesContext context, UIComponent component)
    throws java.io.IOException {
        ResponseWriter writer = context.getResponseWriter();

        Map attrs = component.getAttributes();
        String id = (String) attrs.get("for");

        UIInput input = null;
        
        if (!StringUtils.isEmpty(id)) {
            input = (UIInput) component.findComponent(id);
        }

        if ((input != null) && input.isRequired()) {
            writer.write(" <span class=\"req\">*</span>");
        }

        writer.endElement("label");
    }

    private boolean hasMessages(FacesContext context, UIComponent component) {
        Iterator it = context.getClientIdsWithMessages();
        boolean found = false;

        while (it.hasNext()) {
            String id = (String) it.next();

            if ((component != null) && id.equals(component.getClientId(context))) {
                found = true;
                break;
            }
        }

        return found;
    }
}
