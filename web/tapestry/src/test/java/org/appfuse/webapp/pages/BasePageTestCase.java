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
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

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

    /**
     * Flush search indexes, to be done after a reindex() or reindexAll() operation
     */
    public void flushSearchIndexes() {
        // If using JPA DAO, reindex via FullTextEntityManager
        EntityManagerFactory entityManagerFactory = null;
        try {
            entityManagerFactory = (EntityManagerFactory) applicationContext.getBean("entityManagerFactory");
        } catch (NoSuchBeanDefinitionException ex) {
            // ignore
        }
        if (entityManagerFactory != null) {
            final FullTextEntityManager fullTextEntityMgr = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManagerFactory.createEntityManager());
            fullTextEntityMgr.flushToIndexes();
            return;
        }

        // If using HIBERNATE DAO, reindex via FullTextSession
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
        } catch (NoSuchBeanDefinitionException ex) {
            // ignore
        }
        if (sessionFactory != null) {
            Session currentSession = sessionFactory.getCurrentSession();
            final FullTextSession fullTextSession = org.hibernate.search.Search.getFullTextSession(currentSession);
            fullTextSession.flushToIndexes();
            return;
        }
        log.error("flushSearchIndexes cannot be done: cannot find appropiate bean to obtain FullTextSession");
    }
}