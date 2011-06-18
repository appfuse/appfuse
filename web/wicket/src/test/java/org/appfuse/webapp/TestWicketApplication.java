package org.appfuse.webapp;

import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.springframework.context.ApplicationContext;

public class TestWicketApplication extends WicketApplication {

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
}
