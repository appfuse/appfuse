package org.appfuse.dao;

import org.appfuse.Constants;
import org.appfuse.model.Role;

public class RoleDaoTest extends BaseDaoTestCase {
    private Role role = null;
    private RoleDao dao = null;

    public void setRoleDao(RoleDao dao) {
        this.dao = dao;
    }

    public void testGetRoleInvalid() throws Exception {
        role = dao.getRoleByName("badrolename");
        assertNull(role);
    }

    public void testGetRole() throws Exception {
        role = dao.getRoleByName(Constants.USER_ROLE);
        assertNotNull(role);
    }

    public void testUpdateRole() throws Exception {
        role = dao.getRoleByName("user");
        log.debug(role);
        role.setDescription("test descr");

        dao.saveRole(role);
        assertEquals(role.getDescription(), "test descr");
    }

    public void testAddAndRemoveRole() throws Exception {
        deleteFromTables(new String[]{"user_role", "role"});
        
        role = new Role("testrole");
        role.setDescription("new role descr");

        dao.saveRole(role);
        assertNotNull(role.getName());

        dao.removeRole("testrole");

        endTransaction();

        role = dao.getRoleByName("testrole");
        assertNull(role);
    }
}
