package org.appfuse.webapp.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.spring.SpringConstants;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.Constants;
import org.appfuse.webapp.services.AppTestModule;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

import static junit.framework.Assert.assertTrue;

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

    private final String[] locations = extractLocationFromAnnotation(this.getClass());

    private MockServletContext servletContext;

    private ServletContextListener listener;

    protected ApplicationContext applicationContext;

    @Before
    public void onSetUp() {
        String appPackage = "org.appfuse.webapp";
        String appName = "app";


        servletContext = new MockServletContext("");

        // mock servlet settings
        servletContext.addInitParameter(SpringConstants.USE_EXTERNAL_SPRING_CONTEXT, "true");
        servletContext.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                StringUtils.arrayToCommaDelimitedString(locations)
        );

        // Start context loader w/ mock servlet prior to firing off registry
        listener = new ContextLoaderListener();
        listener.contextInitialized(new ServletContextEvent(servletContext));


        tester = new PageTester(appPackage, appName, "src/main/webapp", AppTestModule.class) {
            @Override
            protected ModuleDef[] provideExtraModuleDefs() {
                return new ModuleDef[]{new SpringModuleDef(servletContext)};
            }
        };

        applicationContext = (WebApplicationContext)
                servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        fieldValues = new HashMap<String, String>();

        smtpPort = smtpPort + (int) (Math.random() * 100);

        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = //(JavaMailSenderImpl)applicationContext.getBean("mailSender");
                applicationContext.getBean(JavaMailSenderImpl.class);
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
    }

    private String[] extractLocationFromAnnotation(Class<?> clazz) {
        ContextConfiguration contextConfiguration = InternalUtils.findAnnotation(clazz.getAnnotations(),
                ContextConfiguration.class);
        String[] locations = null;
        if (contextConfiguration != null) {
            locations = contextConfiguration.locations();
        }

        return locations;
    }

    @After
    public void onTearDown() {
        if (tester != null) {
            tester.shutdown();
        }
        tester = null;
        // cleanup: close sessionFactory and related resources (search index locks)
        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;
        servletContext = null;
    }

    protected int getSmtpPort() {
        return smtpPort;
    }

    protected void assertTextPresent(Document document, String text) {
        assertTrue(document.toString().contains(text));
    }
}
