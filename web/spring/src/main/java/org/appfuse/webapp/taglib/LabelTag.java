package org.appfuse.webapp.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorResources;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContext;
import org.springmodules.validation.commons.ValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * <p>This class is designed to render a <label> tag for labeling your forms and
 * adds an asterik (*) for required fields.  It was originally written by Erik
 * Hatcher (http://www.ehatchersolutions.com/JavaDevWithAnt/).
 * <p/>
 * <p>It is designed to be used as follows:
 * <pre>&lt;tag:label key="userForm.username"/&gt;</pre>
 *
 * @jsp.tag name="label" bodycontent="empty"
 */
public class LabelTag extends TagSupport {
    private static final long serialVersionUID = -5310144023136517119L;
    protected RequestContext requestContext;
    protected transient final Log log = LogFactory.getLog(LabelTag.class);
    protected String key = null;
    protected String styleClass = null;
    protected String errorClass = null;
    protected boolean colon = false;

    public int doStartTag() throws JspException {

        try {
            this.requestContext =
                    new RequestContext((HttpServletRequest) this.pageContext.getRequest());
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            pageContext.getServletContext().log("Exception in custom tag", ex);
        }

        // Look up this key to see if its a field of the current form
        boolean requiredField = false;
        boolean validationError = false;

        ValidatorResources resources = getValidatorResources();

        Locale locale = pageContext.getRequest().getLocale();

        if (locale == null) {
            locale = Locale.getDefault();
        }

        // get the name of the bean from the key
        String formName = key.substring(0, key.indexOf('.'));
        String fieldName = key.substring(formName.length() + 1);

        if (resources != null) {
            Form form = resources.getForm(locale, formName);

            if (form != null) {
                Field field = form.getField(fieldName);

                if (field != null) {
                    if (field.isDependency("required") || field.isDependency("validwhen")) {
                        requiredField = true;
                    }
                }
            }
        }

        Errors errors = requestContext.getErrors(formName, false);
        List fes = null;
        if (errors != null) {
            fes = errors.getFieldErrors(fieldName);
            //String errorMsg = getErrorMessages(fes);
        }

        if (fes != null && fes.size() > 0) {
            validationError = true;
        }

        // Retrieve the message string we are looking for
        String message = null;
        try {
            message = getMessageSource().getMessage(key, null, locale);
        } catch (NoSuchMessageException nsm) {
            message = "???" + key + "???";
        }

        String cssClass = null;
        if (styleClass != null) {
            cssClass = styleClass;
        } else if (requiredField) {
            cssClass = "required";
        }

        String cssErrorClass = (errorClass != null) ? errorClass : "error";
        StringBuilder label = new StringBuilder();

        if ((message == null) || "".equals(message.trim())) {
            label.append("");
        } else {
            label.append("<label for=\"").append(fieldName).append("\"");

            if (cssClass != null) {
                label.append(" class=\"").append(cssClass);
                if (validationError) {
                    label.append(" ").append(cssErrorClass);
                }
            }

            label.append("\">").append(message);
            label.append((requiredField) ? " <span class=\"required\">*</span>" : "");
            label.append((colon) ? ":" : "");
            label.append("</label>");
        }

        // Print the retrieved message to our output writer
        try {
            writeMessage(label.toString());
        } catch (IOException io) {
            io.printStackTrace();
            throw new JspException("Error writing label: " + io.getMessage());
        }

        // Continue processing this page
        return (SKIP_BODY);
    }

    /**
     * Extract the error messages from the given ObjectError list.
     */
    /*private String getErrorMessages(List fes) throws NoSuchMessageException {
        StringBuffer message = new StringBuffer();
        for (int i = 0; i < fes.size(); i++) {
            ObjectError error = (ObjectError) fes.get(i);
            message.append(this.requestContext.getMessage(error, true));
        }
        return message.toString();
    }*/

    /**
     * Write the message to the page.
     *
     * @param msg the message to write
     * @throws IOException if writing failed
     */

    protected void writeMessage(String msg) throws IOException {
        pageContext.getOut().write(msg);
    }

    /**
     * @jsp.attribute required="true" rtexprvalue="true"
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Setter for specifying whether to include colon
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setColon(boolean colon) {
        this.colon = colon;
    }

    /**
     * Setter for assigning a CSS class, default is label.
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * Setter for assigning a CSS class when errors occur,
     * defaults to labelError.
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        key = null;
        colon = false;
        styleClass = null;
        errorClass = null;
        requestContext = null;
    }

/**
 * Do End Tag to clear objects from memory
 */
public final int doEndTag()
{
     super.release();
        key = null;
        colon = false;
        styleClass = null;
        errorClass = null;
        requestContext = null;
        return 1;
    
}


    /**
     * Get the validator resources from a ValidatorFactory defined in the
     * web application context or one of its parent contexts.
     * The bean is resolved by type (org.springframework.validation.commons.ValidatorFactory).
     *
     * @return ValidatorResources from a ValidatorFactory.
     */
    private ValidatorResources getValidatorResources() {
        // look in servlet beans definition (i.e. action-servlet.xml)
        WebApplicationContext ctx = (WebApplicationContext) pageContext.getRequest()
                .getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        ValidatorFactory factory = null;
        try {
            factory = (ValidatorFactory) BeanFactoryUtils
                    .beanOfTypeIncludingAncestors(ctx, ValidatorFactory.class, true, true);
        } catch (NoSuchBeanDefinitionException e) {
            // look in main application context (i.e. applicationContext.xml)
            ctx = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(pageContext.getServletContext());
            factory = (ValidatorFactory) BeanFactoryUtils
                    .beanOfTypeIncludingAncestors(ctx, ValidatorFactory.class, true, true);
        }
        return factory.getValidatorResources();
    }


    /**
     * Use the application context itself for default message resolution.
     */
    private MessageSource getMessageSource() {
        return requestContext.getWebApplicationContext();
    }
}
