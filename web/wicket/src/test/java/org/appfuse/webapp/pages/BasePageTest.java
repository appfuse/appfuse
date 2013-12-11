package org.appfuse.webapp.pages;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.WicketTester;
import org.appfuse.webapp.TestAppFuseWicketApplication;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.StaticWebApplicationContext;

import java.util.Locale;

/**
 * Base class for Wicket integration tests with WicketTester..
 *
 * @author Marcin Zajączkowski, 2011-06-19
 */
public abstract class BasePageTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected WicketTester tester;

    @Before
    public void initTester() {
        StaticWebApplicationContext mockedContext = new StaticWebApplicationContext();

        tester = new WicketTester(new TestAppFuseWicketApplication(mockedContext));
        //ensure english locale regardless of local system locale
        tester.getSession().setLocale(Locale.ENGLISH);

        initSpringBeans(mockedContext);
    }

    protected void initSpringBeans(StaticWebApplicationContext context) {
        MockitoAnnotations.initMocks(this);

        //Subclasses should register (mocked) beans in context e.g.
        // context.getBeanFactory().registerSingleton("myBean", myBean)
    }

    protected void goToPageAndAssertIfRendered(Class<? extends Page> pageClass) {
        tester.startPage(pageClass);
        tester.assertRenderedPage(pageClass);
    }

    protected String getRequiredErrorMessageByField(String field) {
        return "'" + field + "' is required.";
    }

    protected void assertRenderedLoginPageWithErrorMessages(Class<? extends Page> pageClass, String... errorMessage) {
        tester.assertRenderedPage(pageClass);
        tester.assertErrorMessages(errorMessage);
    }
}
