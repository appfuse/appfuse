package org.appfuse.persistence;

import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.model.UserCookie;


public class UserDAOTest extends BaseDAOTestCase {
    private static User savedUser = null;
    private User user = null;
    private UserDAO dao = null;


    protected void setUp() throws Exception {
        log = LogFactory.getLog(UserDAOTest.class);
        dao = (UserDAO) ctx.getBean("userDAO");
    }

    protected void tearDown() throws Exception {
        dao = null;
    }

    public void testGetUserInvalid() throws Exception {
        try {
            user = dao.getUser("badusername");
            fail("'badusername' found in database, failing test...");
        } catch (DAOException d) {
            if (log.isDebugEnabled()) {
                log.debug(d);
            }

            assertTrue(d != null);
        }
    }

    public void testGetUser() throws Exception {
        user = dao.getUser("tomcat");

        assertTrue(user != null);
        assertTrue(user.getRoles().size() == 1);
    }

    public void testSaveUser() throws Exception {
        user = dao.getUser("tomcat");

        //log.info(user);
        user.setAddress("new address");

        dao.saveUser(user);
        assertEquals(user.getAddress(), "new address");
    }

    public void testAddUserRole() throws Exception {
        user = dao.getUser("tomcat");

        assertTrue(user.getRoles().size() == 1);

        user.addRole(Constants.ADMIN_ROLE);
        user = dao.saveUser(user);

        assertTrue(user.getRoles().size() == 2);
        
        // add the same role twice - should result in no additional role
        user.addRole(Constants.ADMIN_ROLE);
        user = dao.saveUser(user);
        
        assertTrue("more than 2 roles [" + user.getRoles().size() + "]", 
                user.getRoles().size() == 2);

        user.getRoles().remove(1);
        user = dao.saveUser(user);

        assertTrue(user.getRoles().size() == 1);
    }

    public void testAddUser() throws Exception {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass");
        user.setFirstName("Test");
        user.setLastName("Last");
        user.setCity("Denver");
        user.setProvince("CO");
        user.setCountry("USA");
        user.setPostalCode("80210");
        user.setEmail("testuser@appfuse.org");
        user.setWebsite("http://raibledesigns.com");
        user.addRole(Constants.USER_ROLE);

        user = dao.saveUser(user);
        assertTrue(user.getUsername() != null);
        assertTrue(user.getPassword().equals("testpass"));
    }

    public void testRemoveUser() throws Exception {
        dao.removeUser("testuser");
        
        try {
            user = dao.getUser("testuser");
            fail("Expected 'DAOException' not thrown");
        } catch (DAOException d) {
            if (log.isDebugEnabled()) {
                log.debug(d);
            }

            assertTrue(d != null);
        }
    }
    
    public void testSaveAndDeleteUserCookie() throws Exception {
        String cookieId = "BA67E786-C031-EA40-2769-863BB30B31EC";
        UserCookie cookie = new UserCookie();
        cookie.setUsername("tomcat");
        cookie.setCookieId(cookieId);
        dao.saveUserCookie(cookie);
        cookie = dao.getUserCookie(cookieId);
        assertEquals(cookieId, cookie.getCookieId());
        
        dao.removeUserCookies(cookie.getUsername());
        
        cookie = dao.getUserCookie(cookieId);
        assertNull(cookie);
    }
 

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UserDAOTest.class);
    }
}
