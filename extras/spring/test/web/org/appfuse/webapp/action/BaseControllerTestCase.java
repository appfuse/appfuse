package org.appfuse.webapp.action;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

public class BaseControllerTestCase extends TestCase {
    private static Log log = LogFactory.getLog(BaseControllerTestCase.class);
    protected static ApplicationContext ctx;
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

        ctx = new ClassPathXmlApplicationContext(paths);
        login = ResourceBundle.getBundle(LoginServletTest.class.getName());
    }

    protected void setUp() throws Exception {
        // populate the userForm and place into session
        String username = login.getString("username");
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
        user = (User) userMgr.getUser(username);
    }

    protected void tearDown() {
        ctx = null;
    }
    
    /**
     * Convenience methods to make tests simpler
     */
    public MockHttpServletRequest newPost(String url) {
        return new MockHttpServletRequest("POST", url);   
    }

    public MockHttpServletRequest newGet(String url) {
        return new MockHttpServletRequest("GET", url);   
    }
    
    public void objectToRequestParameters(Object o, MockHttpServletRequest request) {
        Class clazz = o.getClass();

        Field[] fields = clazz.getDeclaredFields();

        try {
            AccessibleObject.setAccessible(fields, true);

            for (int i = 0; i < fields.length; i++) {
                if (!(fields[i].get(o) instanceof List)) {
                    request.addParameter(fields[i].getName(), 
                            String.valueOf(fields[i].get(o)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}