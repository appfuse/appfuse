package org.appfuse.webapp.action;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.BaseObject;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

public abstract class BaseControllerTestCase extends TestCase {
    protected transient final Log log = LogFactory.getLog(getClass());
    protected static XmlWebApplicationContext ctx;
    protected User user;

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        String[] paths = {
                "classpath*:/" + pkg + "/dao/applicationContext-*.xml",
                "classpath*:META-INF/applicationContext-*.xml",
                "/WEB-INF/applicationContext-*.xml",
                "/WEB-INF/action-servlet.xml"
            };

        ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(paths);
        ctx.setServletContext(new MockServletContext(""));
        ctx.refresh();
    }

    protected void setUp() throws Exception {
        // populate the userForm and place into session
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
        user = (User) userMgr.getUser("tomcat");

        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
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

    public void objectToRequestParameters(Object o, MockHttpServletRequest request) throws Exception {
        objectToRequestParameters(o, request, null);
    }

    public void objectToRequestParameters(Object o, MockHttpServletRequest request, String prefix) throws Exception {
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);

        for (int i = 0; i < fields.length; i++) {
            Object field = (fields[i].get(o));
            if (field instanceof BaseObject) {
                objectToRequestParameters(field, request, fields[i].getName());
            } else if (!(field instanceof List) && !(field instanceof Set)) {
                String paramName = fields[i].getName();
                if (prefix != null) {
                    paramName = prefix + "." + paramName;
                }
                request.addParameter(paramName,
                        String.valueOf(fields[i].get(o)));
            }
        }
    }
}
