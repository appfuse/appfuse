package org.appfuse.dao;

import org.appfuse.Constants;
import org.appfuse.model.Address;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class UserDAOTest extends BaseDAOTestCase {
    private UserDAO dao = null;
    private User user = null;
    private RoleDAO rdao = null;
    private Role role = null;
    
    public void setUserDAO(UserDAO dao) {
        this.dao = dao;
    }
    
    public void setRoleDAO(RoleDAO rdao) {
        this.rdao = rdao;
    }

    public void testGetUserInvalid() throws Exception {
        try {
            user = dao.getUser("badusername");
            fail("'badusername' found in database, failing test...");
        } catch (DataAccessException d) {
            assertTrue(d != null);
        }
    }

    public void testGetUser() throws Exception {
        user = dao.getUser("tomcat");

        assertNotNull(user);
        assertEquals(1, user.getRoles().size());
    }

    public void testUpdateUser() throws Exception {
        user = dao.getUser("tomcat");

        Address address = user.getAddress();
        address.setAddress("new address");

        dao.saveUser(user);

        assertEquals(user.getAddress(), address);
        assertEquals("new address", user.getAddress().getAddress());
        
        // verify that violation occurs when adding new user
        // with same username
        user.setVersion(null);
        
        try {
            dao.saveUser(user);
            fail("saveUser didn't throw DataIntegrityViolationException");
        } catch (DataIntegrityViolationException e) {
            assertNotNull(e);
            log.debug("expected exception: " + e.getMessage());
        }
    }

    public void testAddUserRole() throws Exception {
        user = dao.getUser("tomcat");

        assertEquals(1, user.getRoles().size());

        role = rdao.getRole(Constants.ADMIN_ROLE);
        user.addRole(role);
        dao.saveUser(user);

        assertEquals(2, user.getRoles().size());

        //add the same role twice - should result in no additional role
        user.addRole(role);
        dao.saveUser(user);

        assertEquals("more than 2 roles", 2, user.getRoles().size());

        user.getRoles().remove(role);
        dao.saveUser(user);

        assertEquals(1, user.getRoles().size());
    }

    public void testAddAndRemoveUser() throws Exception {
        user = new User("testuser");
        user.setPassword("testpass");
        user.setFirstName("Test");
        user.setLastName("Last");
        Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");
        user.setAddress(address);
        user.setEmail("testuser@appfuse.org");
        user.setWebsite("http://raibledesigns.com");
        user.addRole(rdao.getRole(Constants.USER_ROLE));

        dao.saveUser(user);

        assertNotNull(user.getUsername());
        assertEquals("testpass", user.getPassword());

        dao.removeUser("testuser");

        try {
            user = dao.getUser("testuser");
            fail("getUser didn't throw DataAccessException");
        } catch (DataAccessException d) {
            assertNotNull(d);
        }
    }
}