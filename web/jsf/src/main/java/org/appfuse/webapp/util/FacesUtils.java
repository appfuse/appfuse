package org.appfuse.webapp.util;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Utility class for JavaServer Faces. Found in JavaWorld article:
 * http://www.javaworld.com/javaworld/jw-07-2004/jw-0719-jsf.html
 *
 * @author <a href="mailto:derek_shen@hotmail.com">Derek Y. Shen</a>
 */
public class FacesUtils {
    /**
     * Get servlet context.
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance()
                                            .getExternalContext().getContext();
    }

    /**
     * Get managed bean based on the bean name.
     *
     * @param beanName the bean name
     * @return the managed bean associated with the bean name
     */
    public static Object getManagedBean(String beanName) {
        Object o =
            getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());

        return o;
    }

    /**
     * Remove the managed bean based on the bean name.
     *
     * @param beanName the bean name of the managed bean to be removed
     */
    public static void resetManagedBean(String beanName) {
        getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(),
                                                     null);
    }

    /**
     * Store the managed bean inside the session scope.
     *
     * @param beanName the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    @SuppressWarnings("unchecked")
    public static void setManagedBeanInSession(String beanName,
                                               Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .put(beanName, managedBean);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext()
                                    .getRequestParameterMap().get(name);
    }

    /**
     * Add information message.
     *
     * @param msg the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    /**
     * Add information message to a sepcific client.
     *
     * @param clientId the client id
     * @param msg the information message
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                                                     new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                      msg, msg));
    }

    /**
     * Add error message.
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    /**
     * Add error message to a sepcific client.
     *
     * @param clientId the client id
     * @param msg the error message
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                                                     new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                      msg, msg));
    }

    /**
     * Evaluate the integer value of a JSF expression.
     *
     * @param el the JSF expression
     * @return the integer value associated with the JSF expression
     */
    public static Integer evalInt(String el) {
        if (el == null) {
            return null;
        }

        if (UIComponentTag.isValueReference(el)) {
            Object value = getElValue(el);

            if (value == null) {
                return null;
            } else if (value instanceof Integer) {
                return (Integer) value;
            } else {
                return new Integer(value.toString());
            }
        } else {
            return new Integer(el);
        }
    }

    private static Application getApplication() {
        ApplicationFactory appFactory =
            (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        return appFactory.getApplication();
    }

    private static ValueBinding getValueBinding(String el) {
        return getApplication().createValueBinding(el);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                                                .getExternalContext()
                                                .getRequest();
    }
    
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance()
                                                .getExternalContext()
                                                .getResponse();
    }
    
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                                                .getExternalContext()
                                                .getSession(false);
    }

    private static Object getElValue(String el) {
        return getValueBinding(el).getValue(FacesContext.getCurrentInstance());
    }

    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }
}
