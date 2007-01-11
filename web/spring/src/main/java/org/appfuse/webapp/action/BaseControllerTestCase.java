package org.appfuse.webapp.action;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.BaseObject;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.util.DateUtil;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

public abstract class BaseControllerTestCase extends AbstractTransactionalDataSourceSpringContextTests {
    protected transient final Log log = LogFactory.getLog(getClass());
    protected User user;

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] {
                "classpath*:/applicationContext-dao.xml",
                "classpath*:/applicationContext-service.xml",
                "/WEB-INF/applicationContext*.xml",
                "/WEB-INF/dispatcher-servlet.xml"
            };
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        // populate the userForm and place into session
        UserManager userMgr = (UserManager) applicationContext.getBean("userManager");
        user = userMgr.getUserByUsername("tomcat");

        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        user = null;
    }

    /**
     * Subclasses can invoke this to get a context key for the given location.
     * This doesn't affect the applicationContext instance variable in this class.
     * Dependency Injection cannot be applied from such contexts.
     */
    protected ConfigurableApplicationContext loadContextLocations(String[] locations) {
        if (logger.isInfoEnabled()) {
            logger.info("Loading additional configuration from: " + StringUtils.arrayToCommaDelimitedString(locations));
        }
        XmlWebApplicationContext ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(locations);
        ctx.setServletContext(new MockServletContext());
        ctx.refresh();
        return ctx;
    }
    
    /**
     * Convenience methods to make tests simpler
     * @return a MockHttpServletRequest with a POST to the specified URL
     * @param url the URL to post to
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

        for (Field f : fields) {
            Object field = (f.get(o));
            if (field != null) {
                if (field instanceof BaseObject) {
                    // Fix for http://issues.appfuse.org/browse/APF-429
                    if (prefix != null) {
                        objectToRequestParameters(field, request, prefix + "." + f.getName());
                    } else {
                        objectToRequestParameters(field, request, f.getName());
                    }
                } else if (!(field instanceof List) && !(field instanceof Set)) {
                    String paramName = f.getName();

                    if (prefix != null) {
                        paramName = prefix + "." + paramName;
                    }

                    String paramValue = String.valueOf(f.get(o));

                    // handle Dates
                    if (field instanceof Date) {
                        paramValue = DateUtil.convertDateToString((Date) f.get(o));
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
