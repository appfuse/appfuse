package org.appfuse.webapp.tapestry;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * This class overrides the default FieldLabel component from Tapestry
 * to allow the labelPrefix and labelSuffix to be written out within
 * the <label> tag.
 */
public abstract class FieldLabel extends org.apache.tapestry.valid.FieldLabel {

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        IForm form = TapestryUtils.getForm(cycle, this);
        IFormComponent field = getField();

        if (field != null && isPrerender())
            form.prerenderField(writer, field, getLocation());

        if (cycle.isRewinding())
            return;

        String displayName = getDisplayName();

        if (displayName == null) {
            if (field == null)
                throw Tapestry.createRequiredParameterException(this, "field");

            displayName = field.getDisplayName();

            if (displayName == null)
                throw new BindingException(ValidMessages.noDisplayName(this, field), this, null,
                        getBinding("field"), null);
        }

        IValidationDelegate delegate = form.getDelegate();
        String id = field == null ? null : field.getClientId();

        // delegate.writeLabelPrefix(field, writer, cycle);

        writer.begin("label");
        delegate.writeLabelPrefix(field, writer, cycle);

        if (id != null) {
            writer.attribute("for", id);
        }

        delegate.writeLabelAttributes(writer, cycle, field);
        renderInformalParameters(writer, cycle);
        writer.print(displayName, getRaw());

        delegate.writeLabelSuffix(field, writer, cycle);

        writer.end();

        // delegate.writeLabelSuffix(field, writer, cycle);
    }
}