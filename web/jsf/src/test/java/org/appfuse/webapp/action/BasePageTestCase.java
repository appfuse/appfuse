package org.appfuse.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.test.mock.MockApplication;
import org.apache.shale.test.mock.MockExternalContext;
import org.apache.shale.test.mock.MockFacesContext;
import org.apache.shale.test.mock.MockFacesContextFactory;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockLifecycle;
import org.apache.shale.test.mock.MockLifecycleFactory;
import org.apache.shale.test.mock.MockRenderKit;
import org.apache.shale.test.mock.MockServletConfig;
import org.apache.shale.test.mock.MockServletContext;
import org.appfuse.Constants;
import org.junit.After;
import org.junit.Before;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * <p>Abstract JUnit test case base class, which sets up the JavaServer Faces
 * mock object environment for a particular simulated request.  The following
 * protected variables are initialized in the <code>setUp()</code> method, and
 * cleaned up in the <code>tearDown()</code> method:</p>
 * <ul>
 * <li><code>application</code> (<code>MockApplication</code>)</li>
 * <li><code>config</code> (<code>MockServletConfig</code>)</li>
 * <li><code>externalContext</code> (<code>MockExternalContext</code>)</li>
 * <li><code>facesContext</code> (<code>MockFacesContext</code>)</li>
 * <li><code>lifecycle</code> (<code>MockLifecycle</code>)</li>
 * <li><code>request</code> (<code>MockHttpServletRequest</code></li>
 * <li><code>response</code> (<code>MockHttpServletResponse</code>)</li>
 * <li><code>servletContext</code> (<code>MockServletContext</code>)</li>
 * <li><code>session</code> (<code>MockHttpSession</code>)</li>
 * </ul>
 * <p/>
 * <p>In addition, appropriate factory classes will have been registered with
 * <code>javax.faces.FactoryFinder</code> for <code>Application</code> and
 * <code>RenderKit</code> instances.  The created <code>FacesContext</code>
 * instance will also have been registered in the apppriate thread local
 * variable, to simulate what a servlet container would do.</p>
 * <p/>
 * <p><strong>WARNING</strong> - If you choose to subclass this class, be sure
 * your <code>onSetUp()</code> and <code>onTearDown()</code> methods call
 * <code>super.setUp()</code> and <code>super.tearDown()</code> respectively,
 * and that you implement your own <code>suite()</code> method that exposes
 * the test methods for your test case.</p>
 * <p/>
 * <p><strong>NOTE:</strong> This class is a copy of Shale's AbstractJsfTestCase,
 * except it extends Spring's AbstractTransactionalDataSourceSpringContextTests
 * instead of JUnit's TestCase.</p>
 */
@ContextConfiguration(
        locations = {"classpath:/applicationContext-resources.xml",
                "classpath:/applicationContext-dao.xml",
                "classpath:/applicationContext-service.xml",
                "classpath*:/applicationContext.xml",
                "classpath:**/applicationContext*.xml"})
public abstract class BasePageTestCase extends AbstractTransactionalJUnit4SpringContextTests {
    protected final Log log = LogFactory.getLog(getClass());
    
    protected MockApplication application = null;
    protected MockServletConfig config = null;
    protected MockExternalContext externalContext = null;
    protected MockFacesContext facesContext = null;
    protected MockFacesContextFactory facesContextFactory = null;
    protected MockLifecycle lifecycle = null;
    protected MockLifecycleFactory lifecycleFactory = null;
    protected MockRenderKit renderKit = null;
    protected MockHttpServletRequest request = null;
    protected MockHttpServletResponse response = null;
    protected MockServletContext servletContext = null;
    protected MockHttpSession session = null;

    // Thread context class loader saved and restored after each test
    private ClassLoader threadContextClassLoader = null;
    private static int smtpPort = 25250;

    /**
     * <p>Set up instance variables required by this test case.</p>
     */
    @Before
    public void onSetUp() {
        smtpPort = smtpPort + (int) (Math.random() * 100);
        
        // Set up a new thread context class loader
        threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0],
                this.getClass().getClassLoader()));

        // Set up Servlet API Objects
        servletContext = new MockServletContext();
        
        config = new MockServletConfig(servletContext);
        session = new MockHttpSession();
        session.setServletContext(servletContext);
        request = new MockHttpServletRequest(session);
        request.setServletContext(servletContext);
        request.setLocale(new Locale("en"));
        response = new MockHttpServletResponse();

        // Set up JSF API Objects
        FactoryFinder.releaseFactories();
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                "org.apache.shale.test.mock.MockApplicationFactory");
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                "org.apache.shale.test.mock.MockFacesContextFactory");
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                "org.apache.shale.test.mock.MockLifecycleFactory");
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                "org.apache.shale.test.mock.MockRenderKitFactory");

        externalContext = new MockExternalContext(servletContext, request, response);
        lifecycleFactory = (MockLifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = (MockLifecycle) lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        facesContextFactory = (MockFacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        facesContext = (MockFacesContext)
                facesContextFactory.getFacesContext(servletContext, request, response, lifecycle);
        
        externalContext = (MockExternalContext) facesContext.getExternalContext();
        UIViewRoot root = new UIViewRoot();
        root.setViewId("/viewId");
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(root);
        ApplicationFactory applicationFactory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication) applicationFactory.getApplication();
        facesContext.setApplication(application);
        RenderKitFactory renderKitFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderKit);

        application.setMessageBundle(Constants.BUNDLE_KEY);

        // set supported locales
        List<Locale> list = new ArrayList<Locale>();
        list.add(new Locale("en"));
        list.add(new Locale("fr"));
        list.add(new Locale("de"));
        list.add(new Locale("es"));
        application.setSupportedLocales(list);

        // change the port on the mailSender so it doesn't conflict with an
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
        mailSender.setPort(getSmtpPort());
        mailSender.setHost("localhost");
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    /**
     * <p>Tear down instance variables required by this test case.</p>
     */
    @After
    public void onTearDown() {
        application = null;
        config = null;
        externalContext = null;
        facesContext.release();
        facesContext = null;
        lifecycle = null;
        lifecycleFactory = null;
        renderKit = null;
        request = null;
        response = null;
        servletContext = null;
        session = null;
        FactoryFinder.releaseFactories();

        Thread.currentThread().setContextClassLoader(threadContextClassLoader);
        threadContextClassLoader = null;
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