package org.appfuse.webapp.taglib;

import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorResources;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.FormTag;
import org.apache.struts.validator.ValidatorPlugIn;


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
    protected transient final Log log = LogFactory.getLog(LabelTag.class);
    protected String key = null;
    protected String styleClass = null;
    protected String errorClass = null;
    protected boolean colon = true;
    protected boolean helpTip = false;

    public int doStartTag() throws JspException {
        // Look up this key to see if its a field of the current form
        boolean requiredField = false;
        boolean validationError = false;
        TagUtils tagUtils = TagUtils.getInstance();

        ValidatorResources resources =
            (ValidatorResources) pageContext.getServletContext()
                .getAttribute(ValidatorPlugIn.VALIDATOR_KEY);
        
        Locale locale = 
            (Locale) pageContext.findAttribute(Globals.LOCALE_KEY);

        if (locale == null) {
            locale = Locale.getDefault();
        }
        
        FormTag formTag =
            (FormTag) pageContext.getAttribute(Constants.FORM_KEY,
                                               PageContext.REQUEST_SCOPE);

        String formName = formTag.getBeanName();
        String fieldName = key.substring(formName.length() + 1);

        if (resources != null) {
            Form form = resources.getForm(locale, formName);

            if (form != null) {
                Field field = form.getField(fieldName);

                if (field != null) {
                    if (field.isDependency("required")) {
                        requiredField = true;
                    }
                }
            }
        }

        ActionMessages errors =
            tagUtils.getActionMessages(pageContext, Globals.ERROR_KEY);

        StringBuffer valMessage = new StringBuffer();

        if (errors != null) {
            // check for errors from the validator
            Iterator valIterator = errors.get(fieldName);

            while (valIterator.hasNext()) {
                validationError = true;

                ActionMessage error = (ActionMessage) valIterator.next();

                // Retrieve the message string we are looking for
                valMessage.append(tagUtils.message(pageContext,
                                                   Globals.MESSAGES_KEY,
                                                   locale.getDisplayName(),
                                                   error.getKey(),
                                                   error.getValues()));
            }
        }

        // Retrieve the message string we are looking for
        String message =
            tagUtils.message(pageContext, Globals.MESSAGES_KEY,
                             locale.getDisplayName(), key);

        StringBuffer valError = new StringBuffer();

        if (message == null) {
            message = "???" + key + "???";
        } else if (validationError) {
            valError.append(valMessage);
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

            if (valError.length() > 0) {
                String error = valError.toString();

                if (helpTip) {
                    // strip out any single or double quotes
                    String htmlFriendly = StringUtils.replace(error, "'", "\\\'");
                    htmlFriendly =
                        StringUtils.replace(htmlFriendly, "\"", "\\\"");
                    label.append(" <a class=\"errorLink\" href=\"?\" onclick=\"showHelpTip(event, '");
    
                    label.append(htmlFriendly + "', false); return false\" ");
                    label.append("onmouseover=\"showHelpTip(event, '");
                    label.append(htmlFriendly + "', false); return false\" ");
                    label.append("onmouseout=\"hideHelpTip(event); return false\">");
                }
                
                
                label.append("<img class=\"validationWarning\" alt=\"");
                label.append(tagUtils.message(pageContext,
                                              Globals.MESSAGES_KEY,
                                              locale.getDisplayName(),
                                              "icon.warning"));
                label.append("\" ");

                String context =
                    ((HttpServletRequest) pageContext.getRequest())
                    .getContextPath();

                label.append("src=\"" + context);
                label.append(tagUtils.message(pageContext,
                                              Globals.MESSAGES_KEY,
                                              locale.getDisplayName(),
                                              "icon.warning.img"));
                label.append("\" />");
                if (helpTip) {
                    label.append("</a>");
                }
            }
        }

        // Print the retrieved message to our output writer
        tagUtils.write(pageContext, label.toString());

        // Continue processing this page
        return (SKIP_BODY);
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
     * Setter for displaying a JavaScript popup helptip.  Default
     * is false because error text is shown next to field.
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setHelpTip(boolean helpTip) {
        this.helpTip = helpTip;
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
        helpTip = false;
    }
}
