package org.appfuse.service;

import java.io.Serializable;
import java.util.List;

public interface UniversalManager {
    /**
     * Generic method used to get a all objects of a particular type. 
     * @param clazz the type of objects 
     * @return List of populated objects
     */
    public List getAll(Class clazz);
    
    /**
     * Generic method to get an object based on class and identifier. 
     * 
     * @param clazz model class to lookup
     * @param id the identifier (primary key) of the class
     * @return a populated object 
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    public Object get(Class clazz, Serializable id);

    /**
     * Generic method to save an object.
     * @param o the object to save
     */
    public Object save(Object o);

    /**
     * Generic method to delete an object based on class and id
     * @param clazz model class to lookup
     * @param id the identifier of the class
     */
    public void remove(Class clazz, Serializable id);
}
