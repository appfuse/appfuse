package org.appfuse.dao;

import java.util.List;

/**
 * This class tests the current LookupDao implementation class
 * @author mraible
 */
public class LookupDaoTest extends BaseDaoTestCase {
    private LookupDao dao;
    
    public void setLookupDao(LookupDao dao) {
        this.dao = dao;
    }

    public void testGetRoles() {
        List roles = dao.getRoles();
        log.debug(roles);
        assertTrue(roles.size() > 0);
    }
}
