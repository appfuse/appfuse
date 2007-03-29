package org.appfuse.dao;

import org.appfuse.Constants;
import org.appfuse.model.Role;

public class RoleDaoTest extends BaseDaoTestCase {
    private RoleDao dao;

    public void setRoleDao(RoleDao dao) {
        this.dao = dao;
    }

    public void testGetRoleInvalid() throws Exception {
        Role role = dao.getRoleByName("badrolename");
        assertNull(role);
    }

    public void testGetRole() throws Exception {
        Role role = dao.getRoleByName(Constants.USER_ROLE);
        assertNotNull(role);
    }

    public void testUpdateRole() throws Exception {
        Role role = dao.getRoleByName("user");
        log.debug(role);
        role.setDescription("test descr");

        role = dao.save(role);
        assertEquals(role.getDescription(), "test descr");
    }

    public void testAddAndRemoveRole() throws Exception {
        Role role = new Role("testrole");
        role.setDescription("new role descr");
        role = dao.save(role);
        setComplete(); // change behavior from rollback to commit
        endTransaction();

        startNewTransaction();
        role = dao.getRoleByName("testrole");
        assertNotNull(role.getDescription());

        dao.removeRole("testrole");
        setComplete();
        endTransaction(); // deletes role from database

        role = dao.getRoleByName("testrole");
        assertNull(role);
    }
}
