package org.appfuse.service;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.appfuse.model.User;

import java.util.List;

/**
 * @author mraible
 */
public class GenericManagerTest extends AbstractTransactionalDataSourceSpringContextTests {
    GenericManager<User, Long> userManager = null;

    public void setUserManager(GenericManager<User, Long> genericManager) {
        this.userManager = genericManager;
    }
    
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[]{"/applicationContext-manager.xml",
                "/applicationContext-resources.xml",
                "classpath*:/applicationContext-dao.xml"};
    }

    public void testGetUsers() {
        List<User> users = userManager.getAll();
        assertTrue(users.size() > 0);
    }

    public void testGetUser() {
        User user = userManager.get(1L);
        assertEquals("tomcat", user.getUsername());
    }
}
