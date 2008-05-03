package org.appfuse.dao;

import javax.persistence.EntityNotFoundException;

import org.appfuse.model.User;

import java.util.List;

/**
 * This class tests the generic GenericDao and BaseDao implementation.
 */
public class UniversalDaoTest extends BaseDaoTestCase {
    protected UniversalDao universalDao;

    /**
     * This method is used instead of setUniversalDao b/c setUniversalDao uses
     * autowire byType <code>setPopulateProtectedVariables(true)</code> can also
     * be used, but it's a little bit slower.
     */
    public void onSetUpBeforeTransaction() throws Exception {
        universalDao = (UniversalDao) applicationContext.getBean("universalDao");
    }

    public void onTearDownAfterTransaction() throws Exception {
        universalDao = null;
    }

    @SuppressWarnings("unchecked")
    public void testGetAll() {
        List users = universalDao.getAll(User.class);
        assertEquals(users.size(), 2);
    }    
    
    /**
     * Simple test to verify CRUD works.
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
        user = (User)universalDao.save(user);
        assertNotNull(user.getId());

        // retrieve
        user = (User) universalDao.get(User.class, user.getId());
        assertNotNull(user);
        assertEquals(user.getLastName(), "last");

        // update
        user.getAddress().setCountry("USA");
        universalDao.save(user);
        assertEquals(user.getAddress().getCountry(), "USA");

        // delete
        universalDao.remove(User.class, user.getId());
        try {
            universalDao.get(User.class, user.getId());
            fail("User 'foo' found in database");
        } catch (EntityNotFoundException e) {
            super.assertNotNull(e);
        }
//          catch (ObjectRetrievalFailureException e) {
//            assertNotNull(e.getMessage());
//        } catch (InvalidDataAccessApiUsageException e) { // Spring 2.0 throws this one
//            assertNotNull(e.getMessage());
//        }
    }
}
