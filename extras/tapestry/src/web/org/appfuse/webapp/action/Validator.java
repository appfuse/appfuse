package org.appfuse.webapp.action;

import java.util.Locale;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.RenderString;
import org.apache.tapestry.valid.ValidationDelegate;

/**
 * Custom Validation Delegate - based on one found in Tapestry in Action.
 *
 * @author Matt Raible
 */
public class Validator extends ValidationDelegate {
    public void writeLabelPrefix(IFormComponent component,
                                 IMarkupWriter writer, IRequestCycle cycle) {
        writer.begin("label");

        if (isInError(component)) {
            writer.attribute("class", "error");
        } else {
            writer.attribute("class", "required");
        }

        writer.print(" * ");
    }

    public void writeLabelSuffix(IFormComponent component,
                                 IMarkupWriter writer, IRequestCycle cycle) {
        Locale locale = cycle.getRequestContext().getRequest().getLocale();
        String marker = (locale.equals(Locale.FRENCH)) ? " :" : ":";
        writer.print(marker);
        writer.end();
    }

    public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle,
                                IFormComponent component, IValidator validator) {
        if (isInError()) {
            writer.attribute("class", "error");
        }
    }

    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle,
                            IFormComponent component, IValidator validator) {
        if (isInError(component)) {
            writer.printRaw("&nbsp;");
            writer.begin("img");

            String ctxPath =
                cycle.getRequestContext().getRequest().getContextPath();
            writer.attribute("src", ctxPath + "/images/iconWarning.gif");
            writer.attribute("class", "validationWarning");
            writer.attribute("alt", cycle.getPage().getMessage("icon.warning"));
            writer.end();

            writer.printRaw("&nbsp;");

            IFieldTracking tracking = getComponentTracking();
            IRender render = tracking.getErrorRenderer();
            String error = "";

            if (render instanceof RenderString) {
                error = ((RenderString) render).getString();
            }

            writer.begin("span");
            writer.attribute("class", "fieldError");
            writer.printRaw(error);
            writer.end();
        }
    }
}
