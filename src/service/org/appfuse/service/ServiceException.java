package org.appfuse.service;


/**
 * A general ServiceException that can be thrown to indicate an error
 * in business logic.
 *
 * <p>
 * <a href="ServiceException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class ServiceException extends Exception {
    //~ Constructors ===========================================================

    /**
     * Constructor for ServiceException.
     */
    public ServiceException() {
        super();
    }

    /**
     * Constructor for ServiceException.
     *
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructor for ServiceException.
     *
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for ServiceException.
     *
     * @param cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
