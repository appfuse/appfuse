package org.appfuse.webapp.action;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.opensymphony.xwork.ActionContext;

public class BaseActionTestCase extends TestCase {
    protected transient final Log log = LogFactory.getLog(getClass());
    protected static XmlWebApplicationContext ctx;
    protected static ResourceBundle login;
    protected User user;

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        ResourceBundle db = ResourceBundle.getBundle("database");
        String daoType = db.getString("dao.type");
        String[] paths = {"/applicationContext-database.xml",
                          "/applicationContext-" + daoType + ".xml",
                          "/applicationContext-service.xml",
                          "/action-servlet.xml"};
        ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(paths);
        ctx.setServletContext(new MockServletContext(""));
        ctx.refresh();
        login = ResourceBundle.getBundle(LoginServletTest.class.getName());
    }

    protected void setUp() throws Exception {
        // populate the userForm and place into session
        String username = login.getString("username");
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
        user = (User) userMgr.getUser(username);
        Map attributes = new HashMap();
        attributes.put(Constants.USER_KEY, user);
        ActionContext.getContext().setSession(attributes);
    }
    
    protected void tearDown() throws Exception {
        ActionContext.getContext().setSession(null);   
    }
}