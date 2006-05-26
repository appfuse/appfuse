package org.appfuse.webapp.action;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.RenderString;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationDelegate;

import java.util.Locale;

/**
 * Custom Validation Delegate - based on one found in Tapestry in Action.
 *
 * @author Matt Raible
 */
public class Validator extends ValidationDelegate {
    private static final long serialVersionUID = 6658594142293597652L;

    public void writeLabelAttributes(IMarkupWriter writer, IRequestCycle cycle, IFormComponent component) {
        if (isInError(component)) {
            writer.attribute("class", "error");
        }
    }

    public void writeLabelSuffix(IFormComponent component,
                                 IMarkupWriter writer, IRequestCycle cycle) {
        // TODO: Add logic so required indicator only added to required fields
        writer.printRaw(" <span class=\"req\">*</span>");
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

            String ctxPath = cycle.getInfrastructure().getContextPath();
            writer.attribute("src", ctxPath + "/images/iconWarning.gif");
            writer.attribute("class", "validationWarning");
            writer.attribute("alt", cycle.getPage().getMessages().getMessage("icon.warning"));
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
