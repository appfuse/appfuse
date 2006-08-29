package org.appfuse.service;

import java.io.Serializable;
import java.util.List;

import org.appfuse.dao.Dao;

public interface Manager {
    
    /**
     * Expose the setDao method for testing purposes
     * @param dao
     */
    public void setDao(Dao dao);
    
    /**
     * Generic method used to get a all objects of a particular type. 
     * @param clazz the type of objects 
     * @return List of populated objects
     */
    public List getObjects(Class clazz);
    
    /**
     * Generic method to get an object based on class and identifier. 
     * 
     * @param clazz model class to lookup
     * @param id the identifier (primary key) of the class
     * @return a populated object 
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    public Object getObject(Class clazz, Serializable id);

    /**
     * Generic method to save an object.
     * @param o the object to save
     */
    public void saveObject(Object o);

    /**
     * Generic method to delete an object based on class and id
     * @param clazz model class to lookup
     * @param id the identifier of the class
     */
    public void removeObject(Class clazz, Serializable id);
}
