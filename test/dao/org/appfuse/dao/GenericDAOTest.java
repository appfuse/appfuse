package org.appfuse.dao;

import org.appfuse.model.User;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class tests the generic DAO and BaseDAO implementation.
 */
public class GenericDAOTest extends BaseDAOTestCase {
    protected DAO dao;
    
    /**
     * This method is used instead of setDAO b/c setDAO uses autowire byType
     * <code>setPopulateProtectedVariables(true)</code> can also be used, but it's
     * a little bit slower.
     */
    public void onSetUpBeforeTransaction() throws Exception {
        dao = (DAO) applicationContext.getBean("dao");
    }
    
    public void onTearDownAfterTransaction() throws Exception {
        dao = null;
    }

    /**
     * Simple test to verify BaseDAO works.
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
        assertNotNull(user.getVersion());
        
        // retrieve
        user = (User) dao.getObject(User.class, user.getUsername());
        assertNotNull(user);
        assertEquals(user.getLastName(), "last");
        
        // update
        user.getAddress().setCountry("USA");
        dao.saveObject(user);
        assertEquals(user.getAddress().getCountry(), "USA");
        
        // delete
        dao.removeObject(User.class, user.getUsername());
        try {
            dao.getObject(User.class, "foo");
            fail("User 'foo' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
    }
}
