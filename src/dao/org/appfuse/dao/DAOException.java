package org.appfuse.dao;


/**
 * A general DAOException that is thrown by all DAO classes.
 *
 * <p>
 * <a href="DAOException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class DAOException extends Exception {
    //~ Constructors ===========================================================

    /**
     * Constructor for DAOException.
     */
    public DAOException() {
        super();
    }

    /**
     * Constructor for DAOException.
     *
     * @param message
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructor for DAOException.
     *
     * @param message
     * @param cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for DAOException.
     *
     * @param cause
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
}
