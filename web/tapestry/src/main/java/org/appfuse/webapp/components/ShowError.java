package org.appfuse.webapp.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;

/**
 * Component that displays an error message
 *
 * @author Serge Eby
 * @version $Id: ShowError.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ShowError {

    @Parameter(required = true)
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String aErrorMessage) {
        errorMessage = aErrorMessage;
    }

    final boolean beginRender(MarkupWriter writer) {
        // Skip if empty or null
        if (errorMessage == null || "".equals(errorMessage)) {
            return false;
        }

        writer.element("div",
                "class", "error");
        writer.write(errorMessage);
        writer.end(); // div

        return false;
    }
}
