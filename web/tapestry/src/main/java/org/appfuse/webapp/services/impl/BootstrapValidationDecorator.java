package org.appfuse.webapp.services.impl;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;

/**
 * Custom Implementation of the ValidationDecorator Interface
 * Updated to use Twitter Twitter Bootstrap framework
 *
 * @author Serge Eby
 * @version $Id: BootstrapValidationDecorator.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class BootstrapValidationDecorator implements ValidationDecorator {

    private final Environment environment;
    private final MarkupWriter markupWriter;

    public BootstrapValidationDecorator(Environment environment, MarkupWriter markupWriter) {
        this.environment = environment;
        this.markupWriter = markupWriter;
    }

    public void insideField(Field field) {

    }

    public void beforeLabel(Field field) {

    }

    public void insideLabel(Field field, Element labelElement) {
        if (inError(field)) {
            Element parent = labelElement.getContainer();
            parent.addClassName("has-error");
        }

/*
https://issues.apache.org/jira/browse/TAP5-414  still present

        if (field.isRequired()) {
            labelElement.raw("<span class=\"required\"> *</span>");
        }
*/
    }

    public void afterLabel(Field field) {

    }

    public void beforeField(Field field) {

    }

    public void afterField(Field field) {
        if (inError(field)) {
            markupWriter.element("span",
                    "id", field.getClientId(),
                    "class", "help-block");

            String error = getError(field);
            if (error == null) {
                error = "";
            }
            markupWriter.writeRaw(error);
            markupWriter.end(); // span
        }
    }

    private boolean inError(Field field) {
        ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);
        return tracker.inError(field);
    }

    private String getError(Field field) {
        ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);
        return tracker.getError(field);
    }


}
