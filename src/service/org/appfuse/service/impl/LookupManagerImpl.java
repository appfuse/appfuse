package org.appfuse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.LookupDAO;
import org.appfuse.model.LabelValue;
import org.appfuse.model.Role;
import org.appfuse.service.LookupManager;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * <p><a href="LookupManagerImpl.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupManagerImpl extends BaseManager implements LookupManager {
    //~ Instance fields ========================================================

    private LookupDAO dao;

    //~ Methods ================================================================

    public void setLookupDAO(LookupDAO dao) {
        super.dao = dao;
        this.dao = dao;
    }
    /**
     * @see org.appfuse.service.LookupManager#getAllRoles()
     */
    public List getAllRoles() {
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
