package org.appfuse.webapp.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import org.apache.commons.logging.Log;
import org.apache.struts2.ServletActionContext;
import org.appfuse.Constants;
import org.junit.After;
import org.junit.Before;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * Base class for running Struts 2 Action tests.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@ContextConfiguration(
        locations = {"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml",
                "classpath*:/applicationContext.xml",
                "classpath:**/applicationContext*.xml"})
public abstract class BaseActionTestCase extends AbstractTransactionalJUnit4SpringContextTests {
    /**
     * Transient log to prevent session synchronization issues - children can use instance for logging.
     */
    protected transient final Log log = logger;
    private int smtpPort = 25250;

    @Before
    public void onSetUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);

        LocalizedTextUtil.addDefaultResourceBundle(Constants.BUNDLE_KEY);

        // Initialize ActionContext
        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.addContainerProvider(new XWorkConfigurationProvider());
        Configuration config = configurationManager.getConfiguration();
        Container container = config.getContainer();

        ValueStack stack = container.getInstance(ValueStackFactory.class).createValueStack();
        stack.getContext().put(ActionContext.CONTAINER, container);
        ActionContext.setContext(new ActionContext(stack.getContext()));

        ActionContext.getContext().setSession(new HashMap<String, Object>());

        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");

        // populate the request so getRequest().getSession() doesn't fail in BaseAction.java
        ServletActionContext.setRequest(new MockHttpServletRequest());
    }

    protected int getSmtpPort() {
        return smtpPort;
    }

    @After
    public void onTearDown() {
        ActionContext.getContext().setSession(null);
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
