package org.appfuse.webapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.appfuse.webapp.internal.MenuContext;

/**
 * Menu component. This is essentially generating an unordered list
 * (via ;lt&ul;gt&lt&/ul;gt& tags) wrapping the menu items
 *
 * @author Serge Eby
 */
@SupportsInformalParameters
public class Menu implements ClientElement {

    /**
     * The id parameter - used mainly for css customization
     */
    @Parameter(name = "id", defaultPrefix = BindingConstants.LITERAL)
    private String idParameter;

    /**
     * The menu class name
     */
    @Property
    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
    private String className;


    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String active;

    @Inject
    private JavaScriptSupport javaScriptSupport;


    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;


    private String clientId;


    public String getClientId() {
        return clientId;
    }

    public String getMenuClass() {
        return className;
    }


    void beginRender(MarkupWriter writer) {
        clientId = resources.isBound("id") ? idParameter : javaScriptSupport.allocateClientId(resources);

        Element e = writer.element("ul",

                                   "id", clientId);

        resources.renderInformalParameters(writer);

        if (className != null) {
            e.attribute("class", className);
        }

        environment.push(MenuContext.class, new MenuContext() {
            public boolean isActive(String itemId) {
                return active != null && active.equalsIgnoreCase(itemId);
            }
        });
    }


    void afterRender(MarkupWriter writer) {
        writer.end();    // ul
        environment.pop(MenuContext.class);
    }

}
