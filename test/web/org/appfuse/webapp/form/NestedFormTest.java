package org.appfuse.webapp.form;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.Address;
import org.appfuse.model.User;
import org.appfuse.util.ConvertUtil;

/**
 * @author mraible
 *
 * Test to verify that BeanUtils.copyProperties is working for 
 * nested POJOs and Forms.
 */
public class NestedFormTest extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());
    private User user = null;
    private UserForm userForm = null;
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testCopyPOJOToFormAndBack() throws Exception {
        // pojo -> form
        user = new User();
        user.setId(new Long(1));
        user.setUsername("testuser");
        Address address = new Address();
        address.setCity("Denver");
        user.setAddress(address);
        userForm = (UserForm) ConvertUtil.convert(user);
        assertEquals(userForm.getUsername(), "testuser");
        //log.debug(userForm);
        assertEquals(userForm.getAddressForm().getCity(), "Denver");
        // form -> pojo
        user = new User();
        user = (User) ConvertUtil.convert(userForm);
        assertEquals(user.getUsername(), "testuser");
        assertEquals(user.getAddress().getCity(), "Denver");
    }
}
