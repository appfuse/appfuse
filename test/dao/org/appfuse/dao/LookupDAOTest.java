package org.appfuse.dao;

import java.util.List;

/**
 * This class tests the current LookupDAO implementation class
 * @author mraible
 */
public class LookupDAOTest extends BaseDAOTestCase {
    private LookupDAO dao = null;
    
    protected void setUp() throws Exception {
        dao = (LookupDAO) ctx.getBean("lookupDAO");
    }

    protected void tearDown() {
        dao = null;
    }

    public void testGetRoles() {
        List roles = dao.getRoles();

        if (log.isDebugEnabled()) {
            log.debug(roles);
        }

        assertTrue(roles.size() == 2);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(LookupDAOTest.class);
    }
}
