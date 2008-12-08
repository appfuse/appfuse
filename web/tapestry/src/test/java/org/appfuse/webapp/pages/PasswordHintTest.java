package org.appfuse.webapp.pages;

import org.subethamail.wiser.Wiser;

import java.util.HashMap;
import java.util.Map;

public class PasswordHintTest  extends BasePageTester {

// TODO: Rewrite test case using Tapestry5 built-in features
//    private PasswordHint page;
//
//    @Override
//    protected void onSetUpBeforeTransaction() throws Exception {
//        super.onSetUpBeforeTransaction();
//        // these can be mocked if you want a more "pure" unit test
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("userManager", applicationContext.getBean("userManager"));
//        map.put("mailEngine", applicationContext.getBean("mailEngine"));
//        map.put("mailMessage", applicationContext.getBean("mailMessage"));
//      //  page = (PasswordHint) getPage(PasswordHint.class, map);
//    }
//
//    @Override
//    protected void onTearDownAfterTransaction() throws Exception {
//        super.onTearDownAfterTransaction();
//        page = null;
//    }
//    
//    public void testExecute() throws Exception {
//        // start SMTP Server
//        Wiser wiser = new Wiser();
//        wiser.setPort(getSmtpPort());
//        wiser.start();
//    //    page.execute("user");
//        
//        assertFalse(page.hasErrors());
//
//        // verify an account information e-mail was sent
//        wiser.stop();
//        assertTrue(wiser.getMessages().size() == 1);
//        
//        // verify that success messages are in the request
//  //      assertNotNull(page.getSession().getAttribute("message"));
//    }

}
