package org.appfuse.webapp.action;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.request.RequestContext;

public class MockRequestCycle extends org.apache.tapestry.junit.MockRequestCycle {
    public MockRequestCycle(IEngine engine, RequestContext context) {
        super(engine, context);
    }

    public IPage getPage(String name) {
        // convert the first character to uppercase
        char first = Character.toUpperCase(name.charAt(0));
        name = first + name.substring(1);
        name = MockRequestCycle.class.getPackage().getName() + "." + name;

        try {
            Class clazz = Class.forName(name);
            return (IPage) new AbstractInstantiator().getInstance(clazz);
        } catch (Exception e) {
            // Instantiate a BasePage and hope that works
            try { 
                return (IPage) new AbstractInstantiator().getInstance(BasePage.class);
            } catch (Exception e2) {
                e.printStackTrace();
                throw new RuntimeException("Nope, can't instantiate '" + name +
                                       "'");
            }
        }
    }
    
    // Added to prevent NPE in MockRequestCycle
    public boolean isRewinding() {
        return false;
    }
}
