package org.appfuse.webapp.pages;

import java.util.HashMap;
import java.util.Map;

//import org.apache.tapestry5.engine.ILink;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

public class UserEditTest extends  BasePageTester {
	// TODO: Rewrite test case using Tapestry5 built-in features
//    private UserEdit page;
//
//    @Override
//    protected void onSetUpBeforeTransaction() throws Exception {
//        super.onSetUpBeforeTransaction();        
//        // these can be mocked if you want a more "pure" unit test
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("userManager", applicationContext.getBean("userManager"));
//        map.put("roleManager", applicationContext.getBean("roleManager"));
//        map.put("mailMessage", applicationContext.getBean("mailMessage"));
//        map.put("mailEngine", applicationContext.getBean("mailEngine"));
////        page = (UserForm) getPage(UserForm.class, map);
//    }
//
//    @Override
//    protected void onTearDownAfterTransaction() throws Exception {
//        super.onTearDownAfterTransaction();
//        page = null;
//    }
//    
//    public void testCancel() throws Exception {
//        page.setFrom("");
//  //      ILink link = page.cancel(new MockRequestCycle());
// //       assertEquals("mainMenu" + EXTENSION, link.getURL());
//    }
//    
//    public void testSave() throws Exception {
//        User user = ((UserManager) applicationContext.getBean("userManager")).getUser("-1");
//        user.setPassword("user");
//        user.setConfirmPassword("user");
//        // FIXME: uncomment section below later
////        page.setUser(user);
////
////  //      ILink link = page.save(new MockRequestCycle());
////        assertNotNull(page.getUser());
////        assertFalse(page.hasErrors());
////        assertNull(page.getFrom());
////  //      assertEquals("mainMenu" + EXTENSION, link.getURL());
//    }
//    
//    public void testRemove() throws Exception {
//        User user2Delete = new User();
//        user2Delete.setId(-2L);
//        // FIXME: uncomment section below later
////        page.setUser(user2Delete);
////  //      page.delete(new MockRequestCycle());
////        assertFalse(page.hasErrors());
//    }
}
