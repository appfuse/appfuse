package org.appfuse.webapp.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.request.RequestContext;

public class MockRequestCycle extends org.apache.tapestry.junit.MockRequestCycle {
    private List parameters = new ArrayList();

    public MockRequestCycle(IEngine engine, RequestContext context) {
        super(engine, context);
    }

    public IPage getPage(String name) {
        // convert the first character to uppercase
        char first = Character.toUpperCase(name.charAt(0));
        name = first + name.substring(1);

        // if it ends in an "s", replace "s" with "List"
        if (name.endsWith("s")) {
            name = name.substring(0, name.length() - 1) + "List";
        }

        name = MockRequestCycle.class.getPackage().getName() + "." + name;

        try {
            //log.debug("Instantiating page with class: " + name);
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

    // Allow for setting and retrieving service parameters easier
    public void addServiceParameter(Object value) {
        parameters.add(value);
    }

    public void setServiceParameters(Object[] parameters) {
        throw new UnsupportedOperationException("Not supported, use addServiceParameter instead");
    }

    public Object[] getServiceParameters() {
        return parameters.toArray();
    }
}
