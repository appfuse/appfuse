package org.appfuse.persistence;


/**
 * Data Access Object (DAO) interface.   This is an interface
 * used to tag our DAO classes and to provide common methods to all DAOs.
 *
 * <p>
 * <a href="DAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.4 $ $Date: 2004/05/16 02:16:45 $
 */
public interface DAO {

    /**
     * Generic method to get an object - id must be populated
     * @param o
     * @return a populated object (or null if id doesn't exist)
     * @throws DAOException
     */
    public Object getObject(Object o) throws DAOException;

    /**
     * Generic method to save an object - handles both update and insert.
     * @param o the object to save
     * @throws DAOException
     */
    public Object saveObject(Object o) throws DAOException;

    /**
     * Generic method to delete an object
     * @param o the object to delete
     * @throws DAOException
     */
    public void removeObject(Object o) throws DAOException;
}