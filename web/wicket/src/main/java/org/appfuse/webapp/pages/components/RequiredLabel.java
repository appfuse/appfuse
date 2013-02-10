package org.appfuse.webapp.pages.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Label with required mark component.
 *
 * By default red asterisk is added. Requirement can depend on external state.
 *
 * @author Marcin Zajączkowski, 2011-01-11
 *
 * @deprecated placeholder attribute in an input component can be used instread of a label
 */
@Deprecated
public class RequiredLabel extends Label {

    private boolean required = true;

    public RequiredLabel(String id, String label) {
        super(id, label);
    }

    public RequiredLabel(String id, IModel<?> model) {
        super(id, model);
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {

        if (required) {
            replaceComponentTagBody(markupStream, openTag,
                    getDefaultModelObjectAsString() + getRequiredTag());
        } else {
            super.onComponentTagBody(markupStream, openTag);
        }
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.setName("label");
    }

    /**
     * Returns tag responsible for display required mark.
     *
     * Delegated to a separate method to be able to override.
     *
     * @return tag with required tag
     */
    protected String getRequiredTag() {
        //TODO: MZA: Maybe it's worth to extract it to markup? Problematic.
        return "<span class=\"req\">*</span>";
    }
}
