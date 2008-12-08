package org.appfuse.webapp.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.ExceptionReporter;

/**
 * Customized errror handling page
 *
 * @author Serge Eby
 * @version $Id: Error.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class Error implements ExceptionReporter {
    @Property
    @Persist(PersistenceConstants.FLASH)
    private String error;

    public void reportException(Throwable exception) {
        error = exception.getLocalizedMessage();
    }
}
