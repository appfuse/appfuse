package org.appfuse.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the current LookupDao implementation class
 * @author mraible
 */
public class LookupDaoTest extends BaseDaoTestCase {
    @Autowired
    private LookupDao dao;

    @Test
    public void testGetRoles() {
        List roles = dao.getRoles();
        log.debug(roles);
        assertTrue(roles.size() > 0);
    }
}
