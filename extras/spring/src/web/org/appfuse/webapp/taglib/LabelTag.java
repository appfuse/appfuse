package org.appfuse.webapp.taglib;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorResources;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.commons.ValidatorFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContext;


/**
 * <p>This class is designed to render a <label> tag for labeling your forms and
 * adds an asterik (*) for required fields.  It was originally written by Erik
 * Hatcher (http://www.ehatchersolutions.com/JavaDevWithAnt/).</p>
 *
 * <p>It is designed to be used as follows:
 * <pre>&lt;tag:label key="userForm.username" /&gt;</pre>
 * </p>
 *
 * @jsp.tag name="label" bodycontent="empty"
 */
public class LabelTag extends TagSupport {
   
    protected RequestContext requestContext;
    protected static Log log = LogFactory.getLog(LabelTag.class);
    protected String key = null;
    protected String styleClass = null;
    protected String errorClass = null;
    protected boolean colon = true;

    public int doStartTag() throws JspException {
        
        try {
            this.requestContext =   
                new RequestContext((HttpServletRequest) this.pageContext.getRequest());
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
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
                Field field = (Field) form.getField(fieldName);

                if (field != null) {
                    if (field.isDependency("required")) {
                        requiredField = true;
                    }
                }
            }
        }

		Errors errors = requestContext.getErrors(formName, false);
        List fes = null;
        String errorMsg = null;
        if (errors != null) {
            fes = errors.getFieldErrors(fieldName);
            errorMsg = getErrorMessages(fes);
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
        StringBuffer label = new StringBuffer();

        if ((message == null) || "".equals(message.trim())) {
            label.append("");
        } else {
            label.append("<label for=\"" + fieldName + "\"");

            if (validationError) {
                label.append(" class=\"" + cssErrorClass + "\"");
            } else if (cssClass != null) {
                label.append(" class=\"" + cssClass + "\"");
            }

            label.append(">" + ((requiredField) ? "* " : "") + message);
            label.append(((colon) ? ":" : "") + "</label>");

            if (fes != null && fes.size() > 0) {
                label.append(" <a class=\"errorLink\" href=\"?\" onclick=\"showHelpTip(event, '");

                label.append(errorMsg + "', false); return false\" ");
                label.append("onmouseover=\"showHelpTip(event, '");
                label.append(errorMsg + "', false); return false\" ");
                label.append("onmouseout=\"hideHelpTip(event); return false\">");
                label.append("<img class=\"validationWarning\" alt=\"\" ");

                String context =
                    ((HttpServletRequest) pageContext.getRequest())
                    .getContextPath();

                label.append("src=\"" + context);
                label.append(getMessageSource().getMessage("icon.warning.img", null, locale));
                label.append("\" />");
                label.append("</a>");
            }
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
	private String getErrorMessages(List fes) throws NoSuchMessageException, JspException {
		StringBuffer message = new StringBuffer();
		for (int i = 0; i < fes.size(); i++) {
			ObjectError error = (ObjectError) fes.get(i);
			message.append(this.requestContext.getMessage(error, true));
		}
		return message.toString();
	}

    /**
     * Write the message to the page.
     * @param msg the message to write
     * @throws IOException if writing failed
     */
    protected void writeMessage(String msg) throws IOException {
        pageContext.getOut().write(msg);
    }
    
    /**
     * @jsp.attribute required="true"
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Setter for specifying whether to include colon
     * @jsp.attribute required="false"
     */
    public void setColon(boolean colon) {
        this.colon = colon;
    }

    /**
     * Setter for assigning a CSS class, default is label.
     *
     * @jsp.attribute required="false"
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * Setter for assigning a CSS class when errors occur,
     * defaults to labelError.
     *
     * @jsp.attribute required="false"
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
        colon = true;
        styleClass = null;
        errorClass = null;
    }
    
    /**
     * Get the validator resources from a ValidatorFactory defined in the
     * web application context or one of its parent contexts.
     * The bean is resolved by type (org.springframework.validation.commons.ValidatorFactory).
     *
     * @return ValidatorResources from a ValidatorFactory.
     */
    private ValidatorResources getValidatorResources() {
        ListableBeanFactory lbf = WebApplicationContextUtils
            .getRequiredWebApplicationContext(pageContext.getServletContext());
        ValidatorFactory factory = (ValidatorFactory) BeanFactoryUtils
                .beanOfTypeIncludingAncestors(lbf, ValidatorFactory.class, true, true);
        return factory.getValidatorResources();
    }
    

    /**
     * Use the application context itself for default message resolution.
     */
    private MessageSource getMessageSource() {
        return requestContext.getWebApplicationContext();
    }
    

}
