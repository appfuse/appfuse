package org.appfuse.service;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.LookupDAO;
import org.appfuse.model.Role;
import org.appfuse.service.impl.LookupManagerImpl;
import org.jmock.Mock;


public class LookupManagerTest extends BaseManagerTestCase {
    private LookupManager mgr = new LookupManagerImpl();
    private Mock lookupDAO = null;

    protected void setUp() throws Exception {
        super.setUp();
        lookupDAO = new Mock(LookupDAO.class);
        mgr.setLookupDAO((LookupDAO) lookupDAO.proxy());
    }

    public void testGetAllRoles() {
        if (log.isDebugEnabled()) {
            log.debug("entered 'testGetAllRoles' method");
        }

        // set expected behavior on dao
        Role role = new Role("admin");
        List testData = new ArrayList();
        testData.add(role);
        lookupDAO.expects(once()).method("getRoles")
                 .withNoArguments().will(returnValue(testData));

        List roles = mgr.getAllRoles();
        assertTrue(roles.size() > 0);
        // verify expectations
        lookupDAO.verify();
    }
}
