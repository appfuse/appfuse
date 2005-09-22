package org.appfuse.service;

import java.util.List;

import org.appfuse.dao.LookupDAO;


/**
 * Business Service Interface to talk to persistence layer and
 * retrieve values for drop-down choice lists.
 *
 * <p>
 * <a href="LookupManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface LookupManager extends Manager {
    //~ Methods ================================================================

    public void setLookupDAO(LookupDAO dao);
    
    /**
     * Retrieves all possible roles from persistence layer
     * @return List
     */
    public List getAllRoles();
}
