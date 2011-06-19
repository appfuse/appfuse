package org.appfuse.webapp;

import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class TestWicketApplication extends WicketApplication {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final ApplicationContext testContext;

    public TestWicketApplication(ApplicationContext testContext) {
        this.testContext = testContext;
    }

    //ApplicationContext has to be created completely before WicketApplication class (cannot be created here) 
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
