package org.appfuse.service;

import java.util.List;


/**
 * Business Delegate (Proxy) Interface to talk to persistence layer and
 * retrieve values for drop-down choice lists.
 *
 * <p>
 * <a href="LookupManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:09 $
 */
public interface LookupManager {
    //~ Methods ================================================================

    /**
     * Retrieves all possible roles from persistence layer
     * @return ArrayList
     * @throws Exception
     */
    public List getAllRoles() throws Exception;
}
