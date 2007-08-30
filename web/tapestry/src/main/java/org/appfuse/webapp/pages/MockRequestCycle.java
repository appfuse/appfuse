package org.appfuse.webapp.pages;

import org.apache.tapestry.IPage;
import org.apache.tapestry.engine.RequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.test.Creator;

public class MockRequestCycle extends RequestCycle {
    private String pkg;

    public MockRequestCycle() {
        super();
    }

    public MockRequestCycle(String pkg) {
        super();
        this.pkg = pkg;
    }

    public IPage getPage(String name) {
        String subpackage = null;

        // convert the first character to uppercase if no package name
        if (name.contains("/")) {
            subpackage = name.substring(0, name.indexOf('/'));
            name = name.substring(subpackage.length() + 1);
        }

        char first = Character.toUpperCase(name.charAt(0));
        String className = first + name.substring(1);

        // if it ends in an "s", replace "s" with "List"
        if (className.endsWith("s")) {
            className = className.substring(0, className.length() - 1) + "List";
        }

        String appfusePkg = MockRequestCycle.class.getPackage().getName();

        className = ((pkg != null) ? pkg : appfusePkg) +
                ((subpackage != null) ? "." + subpackage : "") + "." + className;

        IPage page;

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

