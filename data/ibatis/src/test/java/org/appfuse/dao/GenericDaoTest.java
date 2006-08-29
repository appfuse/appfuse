package org.appfuse.dao;

import org.appfuse.model.User;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class tests the generic Dao and BaseDao implementation.
 */
public class GenericDaoTest extends BaseDaoTestCase {
    protected Dao dao;
    
    /**
     * This method is used instead of setDao b/c setDao uses autowire byType
     * <code>setPopulateProtectedVariables(true)</code> can also be used, but it's
     * a little bit slower.
     */
    public void onSetUpBeforeTransaction() throws Exception {
        log.debug(applicationContext);
        dao = (Dao) applicationContext.getBean("dao");
    }
    
    public void onTearDownAfterTransaction() throws Exception {
        dao = null;
    }

    /**
     * Simple test to verify BaseDao works.
     */
    public void testCRUD() {
        User user = new User();
        // set required fields
        user.setUsername("foo");
        user.setPassword("bar");
        user.setFirstName("first");
        user.setLastName("last");
        user.getAddress().setCity("Denver");
        user.getAddress().setPostalCode("80465");
        user.setEmail("foo@bar.com");
        
        // create
        dao.saveObject(user);
        assertNotNull(user.getId());
        
        // retrieve
        user = (User) dao.getObject(User.class, user.getId());
        assertNotNull(user);
        assertEquals(user.getLastName(), "last");
        
        // update
        user.getAddress().setCountry("USA");
        dao.saveObject(user);
        assertEquals(user.getAddress().getCountry(), "USA");
        
        // delete
        dao.removeObject(User.class, user.getId());
        try {
            dao.getObject(User.class, user.getId());
            fail("User 'foo' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
    }
}
