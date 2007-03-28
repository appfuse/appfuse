package org.appfuse.service.impl;

import org.appfuse.dao.UniversalDao;
import org.appfuse.model.User;
import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class tests the generic UniversalManager and UniversalManagerImpl implementation.
 */
public class UniversalManagerTest extends BaseManagerMockTestCase {
    protected UniversalManagerImpl manager = new UniversalManagerImpl();
    protected Mock dao;
    
    protected void setUp() throws Exception {
        super.setUp();
        dao = new Mock(UniversalDao.class);
        manager.setDao((UniversalDao) dao.proxy());
    }
    
    protected void tearDown() throws Exception {
        manager = null;
        dao = null;
    }

    /**
     * Simple test to verify BaseDao works.
     */
    public void testCRUD() {
        User user = new User();
        // set required fields
        user.setUsername("foo");

        // create
        // set expectations
        dao.expects(once()).method("save").will(returnValue(user));
        
        user = (User)manager.save(user);
        dao.verify();
        
        // retrieve
        dao.reset();
        // expectations
        dao.expects(once()).method("get").will(returnValue(user));
        
        user = (User) manager.get(User.class, user.getUsername());
        dao.verify();
        
        // update
        dao.reset();
        dao.expects(once()).method("save").isVoid();
        user.getAddress().setCountry("USA");
        user = (User)manager.save(user);
        dao.verify();
        
        // delete
        dao.reset();
        // expectations
        Exception ex = new ObjectRetrievalFailureException(User.class, "foo");
        dao.expects(once()).method("remove").isVoid();            
        dao.expects(once()).method("get").will(throwException(ex));
        manager.remove(User.class, "foo");
        try {
            manager.get(User.class, "foo");
            fail("User 'foo' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        dao.verify();
    }
}
