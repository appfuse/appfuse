package org.appfuse.service;

import org.appfuse.dao.Dao;
import org.appfuse.model.User;
import org.appfuse.service.impl.BaseManager;
import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class tests the generic Manager and BaseManager implementation.
 */
public class GenericManagerTest extends BaseManagerTestCase {
    protected Manager manager = new BaseManager();
    protected Mock dao;
    
    protected void setUp() throws Exception {
        super.setUp();
        dao = new Mock(Dao.class);
        manager.setDao((Dao) dao.proxy());
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
        dao.expects(once()).method("saveObject").isVoid();
        
        manager.saveObject(user);
        dao.verify();
        
        // retrieve
        dao.reset();
        // expectations
        dao.expects(once()).method("getObject").will(returnValue(user));
        
        user = (User) manager.getObject(User.class, user.getUsername());
        dao.verify();
        
        // update
        dao.reset();
        dao.expects(once()).method("saveObject").isVoid();
        user.getAddress().setCountry("USA");
        manager.saveObject(user);
        dao.verify();
        
        // delete
        dao.reset();
        // expectations
        Exception ex = new ObjectRetrievalFailureException(User.class, "foo");
        dao.expects(once()).method("removeObject").isVoid();            
        dao.expects(once()).method("getObject").will(throwException(ex));
        manager.removeObject(User.class, "foo");
        try {
            manager.getObject(User.class, "foo");
            fail("User 'foo' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        dao.verify();
    }
}
