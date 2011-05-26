package org.appfuse.webapp.services.impl;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;

/**
 * Custom Implementation of the ValidationDecorator Interface
 *
 * @author Serge Eby
 * @version $Id: ValidationDelegate.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ValidationDelegate implements ValidationDecorator {

    private final Environment environment;
    private final Asset iconAsset;
    private final MarkupWriter markupWriter;

    public ValidationDelegate(Environment environment, Asset iconAsset,
                              MarkupWriter markupWriter) {
        this.environment = environment;
        this.markupWriter = markupWriter;
        this.iconAsset = iconAsset;
    }

    public void insideField(Field field) {

        if (inError(field)) {
            if (field.isRequired() && isMissing(field)) {
                addErrorClassToCurrentElement("fieldMissing");
                return;
            }
            addErrorClassToCurrentElement("fieldInvalid");
        }
    }

    public void beforeLabel(Field field) {

    }

    public void insideLabel(Field field, Element labelElement) {
        if (inError(field)) {
            addErrorClassToCurrentElement("error");
        }
        /* http://www.nabble.com/Problem-using-BeanEditForm-with-a-POJO-td23349016s302.html#a23352398
        if (field.isRequired()) {
            labelElement.raw("<span class=\"req\"> *</span>");
        }*/

    }

    public void afterLabel(Field field) {
    }

    public void beforeField(Field field) {
        if (inError(field)) {
            markupWriter.element("span", "class", "fieldError");
            String iconId = field.getClientId() + ":icon";
            markupWriter.element("img", "src", iconAsset.toClientURL(), "alt",
                    "", "class", "validationWarning", "id", iconId);
            markupWriter.end(); // img
            markupWriter.writeRaw("&nbsp;");

            String error = getError(field);
            if (error == null) {
                error = "";
            }
            markupWriter.writeRaw(error);
            markupWriter.end(); // span
        }

    }

    public void afterField(Field field) {
    }

    private boolean inError(Field field) {
        ValidationTracker tracker = environment
                .peekRequired(ValidationTracker.class);

        return tracker.inError(field);
    }

    private String getError(Field field) {
        ValidationTracker tracker = environment
                .peekRequired(ValidationTracker.class);
        return tracker.getError(field);
    }

    private boolean isMissing(Field field) {
        return true; // FIXME: Determine if field wasn't populated
    }

    private void addErrorClassToCurrentElement(String errorClass) {
        markupWriter.getElement().addClassName(errorClass);
    }

}
