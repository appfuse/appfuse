package org.appfuse.dao.jpa;

import javax.persistence.PersistenceException;

import org.appfuse.dao.jpa.mock.MockRole;
import org.appfuse.dao.jpa.mock.MockRoleBadGetter;
import org.appfuse.dao.jpa.mock.MockRoleIdOnAttributeSubSubclass;
import org.appfuse.dao.jpa.mock.MockRoleIdOnAttributeSubclass;
import org.appfuse.dao.jpa.mock.MockRoleIdOnGetterSubSubclass;
import org.appfuse.dao.jpa.mock.MockRoleIdOnGetterSubclass;
import org.appfuse.model.Address;
import org.appfuse.model.LabelValue;
import org.appfuse.model.Role;

import junit.framework.TestCase;

/**
 * @author bnoll
 *
 */
public class DaoUtilsTest extends TestCase {

    public DaoUtilsTest() {
    }
    
    public void testAnnotatedIdentifiedOnGetterClass() throws Exception {
        Role o = new Role();
        Object id = DaoUtils.getPersistentId(o);
        super.assertNull(id);
    }
    
    public void testAnnotatedNotIdentifiedClass() throws Exception {
        Address o = new Address();
        try {
            Object id = DaoUtils.getPersistentId(o);
            super.fail("Expected a PersistenceException to be thrown.");
        } catch (PersistenceException e) {
            super.assertNotNull(e);
        }
    }
    
    public void testNotAnnotatedClass() throws Exception {
        LabelValue o = new LabelValue();
        try {
            Object id = DaoUtils.getPersistentId(o);
            super.fail("Expected a PersistenceException to be thrown.");
        } catch (PersistenceException e) {
            super.assertNotNull(e);
        }
    }
    
    public void testAnnotatedIdentifiedOnAttributeClass() throws Exception {
        MockRole o = new MockRole();
        Object id = DaoUtils.getPersistentId(o);
        super.assertNull(id);
    }
    
    public void testAnnotatedIdentifiedOnGetterOnSubclass() throws Exception {
        MockRoleIdOnGetterSubclass o = new MockRoleIdOnGetterSubclass();
        Object id = DaoUtils.getPersistentId(o);
        super.assertNull(id);
    }
    
    public void testAnnotatedIdentifiedOnAttributeOnSubclass() throws Exception {
        MockRoleIdOnAttributeSubclass o = new MockRoleIdOnAttributeSubclass();
        Object id = DaoUtils.getPersistentId(o);
        super.assertNull(id);
    }
    
    public void testAnnotatedIdentifiedOnGetterOnSubSubclass() throws Exception {
        MockRoleIdOnGetterSubSubclass o = new MockRoleIdOnGetterSubSubclass();
        Object id = DaoUtils.getPersistentId(o);
        super.assertNull(id);
    }
    
    public void testAnnotatedIdentifiedOnAttributeOnSubSubclass() throws Exception {
        MockRoleIdOnAttributeSubSubclass o = new MockRoleIdOnAttributeSubSubclass();
        Object id = DaoUtils.getPersistentId(o);
        super.assertNull(id);
    }
    
    public void testAnnotatedIdentifiedOnAttributeWithBadGetter() throws Exception {
        MockRoleBadGetter o = new MockRoleBadGetter();
        try {
            Object id = DaoUtils.getPersistentId(o);
            super.fail("Expected a PersistenceException to be thrown.");
        } catch (PersistenceException e) {
            super.assertNotNull(e);
        }
    }
    
    
    
}
