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
 * @version $Revision: 1.4 $ $Date: 2004/05/25 06:27:20 $
 */
public interface LookupManager {
    //~ Methods ================================================================

    /**
     * Retrieves all possible roles from persistence layer
     * @return List
     */
    public List getAllRoles();
}
