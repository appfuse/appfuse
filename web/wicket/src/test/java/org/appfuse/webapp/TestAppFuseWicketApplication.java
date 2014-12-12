package org.appfuse.webapp;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.appfuse.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class TestAppFuseWicketApplication extends AppFuseWicketApplication {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final ApplicationContext testContext;

    public TestAppFuseWicketApplication(ApplicationContext testContext) {
        this.testContext = testContext;
    }

    @Override
    protected void init() {
        super.init();
        setTestConfigInServletContext();
    }

    @SuppressWarnings("unchecked")
    private void setTestConfigInServletContext() {
        Map<String, Object> config = (Map<String, Object>) getServletContext().getAttribute(Constants.CONFIG);
        if (config == null) {
            config = new HashMap<>();
            getServletContext().setAttribute(Constants.CONFIG, config);
        }
    }

    //ApplicationContext has to be created completely before AppFuseWicketApplication class (cannot be created here)
    @Override
    protected ApplicationContext getContext() {
        return testContext;
    }

    //WebSessionClass it get by constructor in super class and cannot be passed via constructor of child class
    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return StaticAuthenticatedWebSession.class;
    }

    @Override
    protected void outputDevelopmentModeWarning() {
        //it's ok for tests - no need to print large warning on stderr
        //just display info message to keep developer informed how many times web application is created in tests
        log.info("Starting test web application in development mode");
    }
}
