package org.appfuse.service;

import org.springframework.context.ApplicationContext;


public class UserExistsExceptionTest extends BaseManagerTestCase {
    private UserManager manager = null;
    private ApplicationContext ctx = null;

    public void testOne() {}
    /*
    protected void setUp() throws Exception {
        super.setUp();
        ctx = new ClassPathXmlApplicationContext("/WEB-INF/applicationContext-*.xml");
        manager = (UserManager) ctx.getBean("userManager");
    }

    public void testAddExistingUser() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entered 'testAddExistingUser' method");
        }

        User user = manager.getUser("tomcat");
        // change unique keys
        user.setUsername("foo");
        user.setEmail("bar");
        user.setVersion(null);
        
        // first save should succeed
        manager.saveUser(user);
        
        // set the version to null - this is the new record indicator
        user.setVersion(null);
        
        // try saving as new user, this should fail
        try {
            manager.saveUser(user);
            fail("Duplicate user didn't throw UserExistsException");
        } catch (UserExistsException uee) {
            assertNotNull(uee);
        }
    }*/
}
