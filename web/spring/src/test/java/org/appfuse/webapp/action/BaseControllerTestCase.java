package org.appfuse.webapp.action;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.BaseObject;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.util.DateUtil;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract class BaseControllerTestCase extends TestCase {
    protected transient final Log log = LogFactory.getLog(getClass());
    protected static XmlWebApplicationContext ctx;
    protected User user;

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        String[] paths = {
                "classpath*:/applicationContext-dao.xml",
                "classpath*:/applicationContext-service.xml",
                "/WEB-INF/applicationContext-resources.xml",
                "/WEB-INF/applicationContext-validation.xml",
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
        user = (User) userMgr.getUserByUsername("tomcat");

        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }

    /**
     * Convenience methods to make tests simpler
     * @return a MockHttpServletRequest with a POST to the specified URL
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
        Field[] fields = getDeclaredFields(clazz);
        AccessibleObject.setAccessible(fields, true);

        for (Field field1 : fields) {
            Object field = (field1.get(o));
            if (field != null) {
                if (field instanceof BaseObject) {
                    // Fix for http://issues.appfuse.org/browse/APF-429
                    if (prefix != null) {
                        objectToRequestParameters(field, request, prefix + "." + field1.getName());
                    } else {
                        objectToRequestParameters(field, request, field1.getName());
                    }
                } else if (!(field instanceof List) && !(field instanceof Set)) {
                    String paramName = field1.getName();

                    if (prefix != null) {
                        paramName = prefix + "." + paramName;
                    }

                    String paramValue = String.valueOf(field1.get(o));

                    // handle Dates
                    if (field instanceof Date) {
                        paramValue = DateUtil.convertDateToString((Date) field1.get(o));
                        if ("null".equals(paramValue)) paramValue = "";
                    }

                    request.addParameter(paramName, paramValue);
                }
            }
        }
    }

    private Field[] getDeclaredFields(Class clazz) {
        Field[] f = new Field[0];
        Class superClazz = clazz.getSuperclass();
        Collection<Field> rval = new ArrayList<Field>();
        
        if (superClazz != null) {
            rval.addAll(Arrays.asList(getDeclaredFields(superClazz)));
        }
        
        rval.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return rval.toArray(f);
    }
}
