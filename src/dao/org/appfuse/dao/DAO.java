package org.appfuse.dao;


/**
 * Data Access Object (DAO) interface.   This is an interface
 * used to tag our DAO classes and to provide common methods to all DAOs.
 *
 * <p>
 * <a href="DAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface DAO {

    /**
     * Generic method to get an object - id must be populated
     * @param o
     * @return a populated object (or null if id doesn't exist)
     */
    public Object getObject(Object o);

    /**
     * Generic method to save an object - handles both update and insert.
     * @param o the object to save
     */
    public Object saveObject(Object o);

    /**
     * Generic method to delete an object
     * @param o the object to delete
     */
    public void removeObject(Object o);
}