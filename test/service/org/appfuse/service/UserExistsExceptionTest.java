package org.appfuse.service;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.util.ClassUtils;

public class UserExistsExceptionTest extends AbstractTransactionalDataSourceSpringContextTests {
    private UserManager manager = null;

    public void setUserManager(UserManager userManager) {
        this.manager = userManager;
    }
    
    protected String[] getConfigLocations() {
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        return new String[] {"classpath*:/" + pkg + "/dao/applicationContext-*.xml",
                             "classpath*:/" + pkg + "/service/applicationContext-service.xml",
                             "classpath*:META-INF/applicationContext-*.xml"};
    }

    public void testAddExistingUser() throws Exception {
        logger.debug("entered 'testAddExistingUser' method");

        User user = manager.getUser("1");
        // change unique keys
        user.setUsername("foo");
        user.setEmail("bar");
        user.setRoles(null);
        user.setVersion(null);
        
        // first save should succeed
        manager.saveUser(user);
        
        // create new object with null id - Hibernate doesn't like setId(null)
        User user2 = new User();
        BeanUtils.copyProperties(user, user2);
        user2.setId(null);
        
        // try saving as new user, this should fail
        try {
            manager.saveUser(user2);
            fail("Duplicate user didn't throw UserExistsException");
        } catch (UserExistsException uee) {
            assertNotNull(uee);
        }
    }
}
