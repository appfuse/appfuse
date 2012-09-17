package org.appfuse.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

@ContextConfiguration(
        locations = {"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml",
                "classpath*:/applicationContext.xml", // for modular archetypes
                "/WEB-INF/applicationContext*.xml",
                "/WEB-INF/dispatcher-servlet.xml"})
public abstract class BaseControllerTestCase extends AbstractTransactionalJUnit4SpringContextTests {
    protected transient final Log log = LogFactory.getLog(getClass());
    private int smtpPort = 25250;

    @Before
    public void onSetUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);
        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
    }

    protected int getSmtpPort() {
        return smtpPort;
    }

    /**
     * Convenience methods to make tests simpler
     *
     * @param url the URL to post to
     * @return a MockHttpServletRequest with a POST to the specified URL
     */
    public MockHttpServletRequest newPost(String url) {
        return new MockHttpServletRequest("POST", url);
    }

    public MockHttpServletRequest newGet(String url) {
        return new MockHttpServletRequest("GET", url);
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
