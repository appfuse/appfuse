package org.appfuse.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UniversalDao;
import org.appfuse.service.UniversalManager;

/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 * 
 * <p><a href="UniversalManagerImpl.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UniversalManagerImpl implements UniversalManager {
    protected final Log log = LogFactory.getLog(getClass());
    protected UniversalDao dao = null;
 
    public void setDao(UniversalDao dao) {
        this.dao = dao;
    }
    
    /**
     * @see org.appfuse.service.UniversalManager#get(java.lang.Class, java.io.Serializable)
     */
    public Object get(Class clazz, Serializable id) {
        return dao.get(clazz, id);
    }
    
    /**
     * @see org.appfuse.service.UniversalManager#getAll(java.lang.Class)
     */
    public List getAll(Class clazz) {
        return dao.getAll(clazz);
    }
    
    /**
     * @see org.appfuse.service.UniversalManager#remove(java.lang.Class, java.io.Serializable)
     */
    public void remove(Class clazz, Serializable id) {
        dao.remove(clazz, id);
    }
    
    /**
     * @see org.appfuse.service.UniversalManager#save(java.lang.Object)
     */
    public Object save(Object o) {
        return dao.save(o);
    }
}
