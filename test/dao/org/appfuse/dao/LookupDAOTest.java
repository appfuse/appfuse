package org.appfuse.dao;

import java.util.List;

/**
 * This class tests the current LookupDAO implementation class
 * @author mraible
 */
public class LookupDAOTest extends BaseDAOTestCase {
    private LookupDAO dao;
    
    protected void setUp() throws Exception {
        super.setUp();
        dao = (LookupDAO) ctx.getBean("lookupDAO");
    }

    public void testGetRoles() {
        List roles = dao.getRoles();

        if (log.isDebugEnabled()) {
            log.debug(roles);
        }

        assertTrue(roles.size() > 0);
    }
}
