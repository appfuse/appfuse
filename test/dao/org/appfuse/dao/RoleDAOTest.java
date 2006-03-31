package org.appfuse.dao;

import org.appfuse.Constants;
import org.appfuse.model.Role;

public class RoleDAOTest extends BaseDAOTestCase {
    private Role role = null;
    private RoleDAO dao = null;

    public void setRoleDAO(RoleDAO dao) {
        this.dao = dao;
    }

    public void testGetRoleInvalid() throws Exception {
        role = dao.getRole("badrolename");
        assertNull(role);
    }

    public void testGetRole() throws Exception {
        role = dao.getRole(Constants.USER_ROLE);
        assertNotNull(role);
    }

    public void testUpdateRole() throws Exception {
        role = dao.getRole("user");
        log.debug(role);
        role.setDescription("test descr");

        dao.saveRole(role);
        assertEquals(role.getDescription(), "test descr");
    }

    public void testAddAndRemoveRole() throws Exception {
        role = new Role("testrole");
        role.setDescription("new role descr");

        dao.saveRole(role);
        assertNotNull(role.getName());

        dao.removeRole("testrole");

        endTransaction();

        role = dao.getRole("testrole");
        assertNull(role);
    }
}
