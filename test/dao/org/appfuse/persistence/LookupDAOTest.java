package org.appfuse.persistence;

import java.util.List;

import org.apache.commons.logging.LogFactory;


/**
 * This class tests the current LookupDAO implementation class
 * @author mraible
 */
public class LookupDAOTest extends BaseDAOTestCase {
    private LookupDAO dao = null;

    protected void setUp() throws Exception {
        log = LogFactory.getLog(LookupDAOTest.class);
        dao = (LookupDAO) ctx.getBean("lookupDAO");
    }

    protected void tearDown() throws Exception {
        dao = null;
    }

    public void testGetRoles() throws Exception {
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
