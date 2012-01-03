package org.appfuse.webapp.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.Constants;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.ContextLoader;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.apache.tapestry5.spring.SpringConstants;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

@ContextConfiguration(locations = {
        "classpath:/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
        "classpath:/applicationContext-service.xml", "classpath*:/applicationContext.xml",
        "/WEB-INF/applicationContext*.xml"})
public abstract class BasePageTestCase extends AbstractTransactionalJUnit4SpringContextTests {
    protected PageTester tester;
    protected Document doc;
    protected Map<String, String> fieldValues;
    protected final Log log = LogFactory.getLog(getClass());
    protected static final String MESSAGES = Constants.BUNDLE_KEY;
    private int smtpPort = 25250;

    @Before
    public void onSetUp() {
        String appPackage = "org.appfuse.webapp";
        String appName = "app";


       final MockServletContext servletContext = new MockServletContext("");
       // Workaround for "attribute is not of type WebApplicationContext" error  when using Spring 3.0.6.RELEASE
       // T 5.3.2 will have Spring 3.1.0.RELEASE as dependency out-of-box
       GenericWebApplicationContext wac = (GenericWebApplicationContext)
               BeanUtils.instantiateClass(GenericWebApplicationContext.class);
        wac.setParent(applicationContext);

       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
               wac);
        servletContext.addInitParameter(SpringConstants.USE_EXTERNAL_SPRING_CONTEXT, "true");

        tester = new PageTester(appPackage, appName, "src/main/webapp") {
            @Override
            protected ModuleDef[] provideExtraModuleDefs() {
                return new ModuleDef[]{new SpringModuleDef(servletContext)};
            }
        };


        fieldValues = new HashMap<String, String>();

        smtpPort = smtpPort + (int) (Math.random() * 100);

        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
    }


    @After
    public void onTearDown() {
        if (tester != null) {
            tester.shutdown();
        }
        tester = null;
    }

    protected int getSmtpPort() {
        return smtpPort;
    }
}
