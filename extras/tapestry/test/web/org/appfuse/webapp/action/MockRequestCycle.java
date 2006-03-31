package org.appfuse.webapp.action;

import org.apache.tapestry.IPage;
import org.apache.tapestry.engine.RequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.test.Creator;

public class MockRequestCycle extends RequestCycle {

    public IPage getPage(final String name) {
        // convert the first character to uppercase
        char first = Character.toUpperCase(name.charAt(0));
        String className = first + name.substring(1);

        // if it ends in an "s", replace "s" with "List"
        if (className.endsWith("s")) {
            className = className.substring(0, className.length() - 1) + "List";
        }
        
        className = MockRequestCycle.class.getPackage().getName() + "." + className;
        IPage page = null;
        
        try {
            page = (IPage) new Creator().newInstance(Class.forName(className));
        } catch (Exception e) {
            // Instantiate a BasePage and hope that works
            try {
                page = (IPage) new Creator().newInstance(BasePage.class);
            } catch (Exception e2) {
                e.printStackTrace();
                throw new RuntimeException("Unable to instantiate '" + className + "'");
            }
        }
        
        page.setPageName(name);
        return page;
    }
}
