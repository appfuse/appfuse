package org.appfuse.service;

import java.util.List;


public class LookupManagerTest extends BaseManagerTestCase {
    //~ Instance fields ========================================================

    private LookupManager mgr;

    //~ Methods ================================================================

    protected void setUp() throws Exception {
        super.setUp();
        mgr = (LookupManager) ctx.getBean("lookupManager");
    }

    public void testGetAllRoles() {
        if (log.isDebugEnabled()) {
            log.debug("entered 'testGetAllRoles' method");
        }

        List roles = mgr.getAllRoles();
        assertTrue(roles.size() > 0);

        if (log.isDebugEnabled()) {
            log.debug("roles: " + roles);
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(LookupManagerTest.class);
    }
}
