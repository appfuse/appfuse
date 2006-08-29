package org.appfuse.webapp.action;

import org.apache.tapestry.pages.Exception;

/**
 * A customized exception page; in non-debug mode, it omits displays the main exception display.
 */

public abstract class Error extends Exception {

    public abstract String getError();
    public abstract void setError(String value);
    
    public void setException(Throwable value) {
        super.setException(value);

        String message = value.getMessage();

        if (message == null)
            message = value.getClass().getName();

        setError(message);
    }
}