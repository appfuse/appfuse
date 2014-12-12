package org.appfuse.dao;

import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@Transactional
public class RoleDaoTest extends BaseDaoTestCase {
    @Autowired
    private RoleDao dao;

    @Test
    public void testGetRoleInvalid() throws Exception {
        Role role = dao.getRoleByName("badrolename");
        assertNull(role);
    }

    @Test
    public void testGetRole() throws Exception {
        Role role = dao.getRoleByName(Constants.USER_ROLE);
        assertNotNull(role);
    }

    @Test
    public void testUpdateRole() throws Exception {
        Role role = dao.getRoleByName("ROLE_USER");
        log.debug(role);
        role.setDescription("test descr");

        dao.save(role);
        assertEquals(role.getDescription(), "test descr");
    }

    @Test
    public void testAddAndRemoveRole() throws Exception {
        Role role = new Role("testrole");
        role.setDescription("new role descr");
        dao.save(role);
        //setComplete(); // change behavior from rollback to commit
        //endTransaction();

        //startNewTransaction();
        role = dao.getRoleByName("testrole");
        assertNotNull(role.getDescription());

        dao.removeRole("testrole");
        //setComplete();
        //super.endTransaction(); // deletes role from database

        //super.startNewTransaction();
        role = dao.getRoleByName("testrole");
        assertNull(role);
    }
}
