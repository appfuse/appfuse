package org.appfuse.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.Role;
import org.appfuse.model.LabelValue;
import org.appfuse.persistence.LookupDAO;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * <p>
 * <a href="LookupManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.2 $ $Date: 2004/03/18 20:33:07 $
 */
public class LookupManagerImpl extends BaseManager implements LookupManager {
    //~ Instance fields ========================================================

    private Log log = LogFactory.getLog(LookupManagerImpl.class);
    private LookupDAO dao;

    //~ Methods ================================================================

    public void setLookupDAO(LookupDAO dao) {
        this.dao = dao;
    }
    /**
     * @see org.appfuse.service.LookupManager#getAllRoles()
     */
    public List getAllRoles() throws Exception {
        List roles = dao.getRoles();
        List list = new ArrayList();
        Role role = null;

        for (int i = 0; i < roles.size(); i++) {
            role = (Role) roles.get(i);
            list.add(new LabelValue(role.getName(), role.getName()));
        }

        return list;
    }
}
