package org.appfuse.webapp.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.util.MessageResources;
import org.appfuse.Constants;
import org.appfuse.webapp.form.UserForm;
import org.appfuse.webapp.util.SslUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Implementation of <strong>Action</strong> that contains base methods for
 * logging and conducting pre/post perform actions. This class is intended to
 * be a base class for all Struts actions.
 *
 * <p>
 * <a href="BaseAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:14 $
 */
public class BaseAction extends LookupDispatchAction {
    public static final String SECURE = "secure";

    // The Log instance for this class. 
    private Log log = LogFactory.getLog(BaseAction.class);
    private WebApplicationContext ctx = null;

    /**
     * Convenience method to bind objects in Actions
     * @param name
     * @return
     */
    public Object getBean(String name) {
        if (ctx == null) {
            ctx = WebApplicationContextUtils
            .getRequiredWebApplicationContext(servlet.getServletContext());
        }
        return ctx.getBean(name);
    }
    
    /**
     * Provides the mapping from resource key to method name
     *
     * @return Resource key / method name map
     */
    public Map getKeyMethodMap() {
        Map map = new HashMap();

        ResourceBundle methods =
            ResourceBundle.getBundle("org.appfuse.webapp.action.LookupMethods");

        Enumeration keys = methods.getKeys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            map.put(key, methods.getString(key));
        }

        return map;
    }

    /**
     * Convenience method to initialize messages in a subclass.
     * @param request the current request
     * @return the populated (or empty) messages
     */
    public ActionMessages getMessages(HttpServletRequest request) {
        ActionMessages messages = null;
        HttpSession session = request.getSession();

        if (request.getAttribute(Globals.MESSAGE_KEY) != null) {
            messages =
                (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
            saveMessages(request, messages);
        } else if (session.getAttribute(Globals.MESSAGE_KEY) != null) {
            messages =
                (ActionMessages) session.getAttribute(Globals.MESSAGE_KEY);
            saveMessages(request, messages);
            session.removeAttribute(Globals.MESSAGE_KEY);
        } else {
            messages = new ActionMessages();
        }

        return messages;
    }

    /**
     * Override the execute method in LookupDispatchAction to parse
     * URLs and forward to methods without parameters.  Also will
     * forward to unspecified method when no parameter is present.
     *
     * This is based on the following system:
     *
     * <ul>
     *   <li>edit*.do -> edit method</li>
     *   <li>save*.do -> save method</li>
     *   <li>view*.do -> search method</li>
     * </ul>
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @param form The optional ActionForm bean for this request (if any)
     * @return Describes where and how control should be forwarded.
     * @exception Exception if an error occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {
        if (isCancelled(request)) {
            ActionForward af = cancelled(mapping, form, request, response);

            if (af != null) {
                return af;
            }
        }

        MessageResources resources = getResources(request);

        // Identify the localized message for the cancel button
        String edit = resources.getMessage("button.edit").toLowerCase();
        String save = resources.getMessage("button.save").toLowerCase();
        String search = resources.getMessage("button.search").toLowerCase();
        String view = resources.getMessage("button.view").toLowerCase();
        String[] rules = { edit, save, search, view };

        // Identify the request parameter containing the method name
        String parameter = mapping.getParameter();

        String keyName = request.getParameter(parameter);

        if ((keyName == null) || (keyName.length() == 0)) {
            for (int i = 0; i < rules.length; i++) {
                // apply the rules for automatically appending the method name
                if (request.getServletPath().indexOf(rules[i]) > -1) {
                    return dispatchMethod(mapping, form, request, response,
                                          rules[i]);
                }
            }

            return this.unspecified(mapping, form, request, response);
        }

        // Identify the string to lookup
        String methodName =
            getMethodName(mapping, form, request, response, parameter);

        return dispatchMethod(mapping, form, request, response, methodName);
    }

    /**
     * Convenience method for getting an action form base on it's mapped scope.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     *
     * @return ActionForm the form from the specifies scope, or null if nothing
     *         found
     */
    protected ActionForm getActionForm(ActionMapping mapping,
                                       HttpServletRequest request) {
        ActionForm actionForm = null;

        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                actionForm =
                    (ActionForm) request.getAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();

                actionForm =
                    (ActionForm) session.getAttribute(mapping.getAttribute());
            }
        }

        return actionForm;
    }

    /**
     * Convenience method to get the userForm from the session
     *
     * @param session the current user's session
     * @return the user's populated form from the session
     */
    protected UserForm getUserForm(HttpSession session) {
        // get the user form from the session
        return (UserForm) session.getAttribute(Constants.USER_KEY);
    }

    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     *
     * @return the user's populated form from the session
     */
    public HashMap getConfiguration() {
        return (HashMap) this.getServlet().getServletContext().getAttribute(Constants.CONFIG);
    }

    // --------------------------------------------------------- Public Methods
    // Don't use class variables in Action objects.  These are not session safe.

    /**
     * Method to check and see if https is required for this resource
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @return boolean true if redirection to SSL is needed
     */
    protected boolean checkSsl(ActionMapping mapping,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        String redirectString =
            SslUtil.getRedirectString(request,
                                      getServlet().getServletContext(),
                                      SECURE.equals(mapping.getParameter()));

        if (redirectString != null) {
            log.debug("protocol switch needed, redirecting...");

            try {
                // Redirect the page to the desired URL
                response.sendRedirect(response.encodeRedirectURL(redirectString));

                return true;
            } catch (Exception ioe) {
                log.error("redirect to new protocol failed...");

                // Handle appropriately
            }
        }

        return false;
    }

    /**
     * Convenience method for removing the obsolete form bean.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     */
    protected void removeFormBean(ActionMapping mapping,
                                  HttpServletRequest request) {
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.removeAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();

                session.removeAttribute(mapping.getAttribute());
            }
        }
    }

    /**
     * Convenience method to update a formBean in it's scope
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     * @param form The ActionForm
     */
    protected void updateFormBean(ActionMapping mapping,
                                  HttpServletRequest request, ActionForm form) {
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                HttpSession session = request.getSession();

                session.setAttribute(mapping.getAttribute(), form);
            }
        }
    }

}
