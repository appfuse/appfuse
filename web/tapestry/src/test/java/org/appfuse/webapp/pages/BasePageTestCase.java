package org.appfuse.webapp.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.spring.SpringConstants;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.Constants;
import org.appfuse.webapp.services.AppModule;
import org.appfuse.webapp.services.AppTestModule;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;

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


        final MockServletContext servletContext = new MockServletContext();
        ConfigurableWebApplicationContext wac = new StaticWebApplicationContext();
        // Just setting the parent doesn't seem to work
        // wac.setParent(applicationContext);
        // Workaround below...
        for (String defName : applicationContext.getBeanDefinitionNames()) {
            wac.getBeanFactory().registerSingleton(defName, applicationContext.getBean(defName));
        }

        servletContext.addInitParameter(SpringConstants.USE_EXTERNAL_SPRING_CONTEXT, "true");
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);
        tester = new PageTester(appPackage, appName, "src/main/webapp", AppTestModule.class) {

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