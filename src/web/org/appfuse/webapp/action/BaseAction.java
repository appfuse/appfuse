package org.appfuse.webapp.action;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.util.ConvertUtil;
import org.appfuse.util.CurrencyConverter;
import org.appfuse.util.DateConverter;
import org.appfuse.webapp.util.SslUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Implementation of <strong>Action</strong> that contains base methods for
 * logging and conducting pre/post perform actions. This class is intended to
 * be a base class for all Struts actions.
 * <p/>
 * <a href="BaseAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseAction extends LookupDispatchAction {

    protected transient final Log log = LogFactory.getLog(getClass());
    private static final String SECURE = "secure";
    private static ApplicationContext ctx = null;
    private static Long defaultLong = null;

    /**
     * NEW: added by Jaap
     *
     * Message (key) name of default locale to message key lookup.
     */
    protected Map defaultKeyNameKeyMap = null;

    static {
        ConvertUtils.register(new CurrencyConverter(), Double.class);
        ConvertUtils.register(new DateConverter(), Date.class);
        ConvertUtils.register(new DateConverter(), String.class);
        ConvertUtils.register(new LongConverter(defaultLong), Long.class);
        ConvertUtils.register(new IntegerConverter(defaultLong), Integer.class);
    }

    /**
     * Convenience method to bind objects in Actions
     *
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

        String pkg = this.getClass().getPackage().getName();
        ResourceBundle methods =
                ResourceBundle.getBundle(pkg + ".LookupMethods");

        Enumeration keys = methods.getKeys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            map.put(key, methods.getString(key));
        }

        return map;
    }

    /**
     * @see org.appfuse.util.ConvertUtil#convert(java.lang.Object)
     */
    protected Object convert(Object o) throws Exception {
        return ConvertUtil.convert(o);
    }

    /**
     * @see org.appfuse.util.ConvertUtil#convertLists(java.lang.Object)
     */
    protected Object convertLists(Object o) throws Exception {
        return ConvertUtil.convertLists(o);
    }

    /**
     * Convenience method to initialize messages in a subclass.
     *
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
     * <p/>
     * This is based on the following system:
     * <p/>
     * <ul>
     * <li>edit*.html -> edit method</li>
     * <li>save*.html -> save method</li>
     * <li>view*.html -> search method</li>
     * </ul>
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @param form     The optional ActionForm bean for this request (if any)
     * @return Describes where and how control should be forwarded.
     * @throws Exception if an error occurs
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
        String edit = resources.getMessage(Locale.ENGLISH, "button.edit").toLowerCase();
        String save = resources.getMessage(Locale.ENGLISH, "button.save").toLowerCase();
        String search = resources.getMessage(Locale.ENGLISH, "button.search").toLowerCase();
        String view = resources.getMessage(Locale.ENGLISH, "button.view").toLowerCase();
        String[] rules = {edit, save, search, view};

        // Identify the request parameter containing the method name
        String parameter = mapping.getParameter();

        // don't set keyName unless it's defined on the action-mapping
        // no keyName -> unspecified will be called
        String keyName = null;
        
        if (parameter != null) {
        	keyName = request.getParameter(parameter);
        }
        
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
    protected User getUser(HttpSession session) {
        // get the user form from the session
        return (User) session.getAttribute(Constants.USER_KEY);
    }

    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     *
     * @return the user's populated form from the session
     */
    public Map getConfiguration() {
        Map config = (HashMap) getServlet().getServletContext()
                               .getAttribute(Constants.CONFIG);
        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }
        return config;
    }

    // --------------------------------------------------------- Public Methods
    // Don't use class variables in Action objects.  These are not session safe.

    /**
     * Method to check and see if https is required for this resource
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
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
     * @param form    The ActionForm
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

    /**
     * NEW: added by Jaap
     * <p/>
     * Lookup the key name corresponding to the client request's locale.
     * This method is overwritten here because the key names are hardcoded
     * in the global forwards of the struts-config.xml file. This in turn is required
     * to make the StrutsMenu tags work (amongst others).
     * <p/>
     * Whatever the reason, this hard-coding is breaking the default i18n-mechanism used by the
     * LookupDispatchAction finds the Action-method name.
     * <p/>
     * What I am doing now is to first call the LookupDispatchAction.getLookupMapName method, catching
     * any ServletException that might arise. There are two reasons why <code>getLookupMapName</code> would throw an
     * exception: first because the keyName cannot be found in the resourcebundle associated with the User's Localea and
     * second because no method name is specified for the corresponding key. We're only interested in the first exception,
     * but catching the second doesn't do any harm, it will just get re-thrown.
     *
     * @see org.apache.struts.actions.LookupDispatchAction#getLookupMapName(HttpServletRequest,String,ActionMapping)
     */
    protected String getLookupMapName(HttpServletRequest request,
                                      String keyName,
                                      ActionMapping mapping)
            throws ServletException {

        if (log.isDebugEnabled()) {
            log.debug("BaseAction: getLookupMapName( keyName = " + keyName + " )");
        }

        String methodName = null;

        try {
            this.setLocale(request, request.getLocale());  // MR - added to default to JSTL
            methodName = super.getLookupMapName(request, keyName, mapping);
        } catch (ServletException ex) {
            if (log.isDebugEnabled()) {
                log.debug("BaseAction: keyName not found in resource bundle with locale "
                        + request.getLocale());
            }

            // the keyname is not available in the resource bundle associated with the user's locale
            // --> get find the key name in the default locale's resource bundle
            if (defaultKeyNameKeyMap == null) {
                defaultKeyNameKeyMap = this.initDefaultLookupMap(request);
            }

            // Find the key for the resource
            String key = (String) defaultKeyNameKeyMap.get(keyName);
            if (key == null) {
                if (log.isDebugEnabled()) {
                	log.debug("keyName '" + keyName + "' not found in resource bundle with locale "
                            + request.getLocale());
                }
                return keyName;
                //String message = messages.getMessage("dispatch.resource", mapping.getPath(), keyName);
                //throw new ServletException(message);
            }

            // Find the method name
            methodName = (String) keyMethodMap.get(key);
            if (methodName == null) {
                String message = messages.getMessage("dispatch.lookup", mapping.getPath(), key);
                throw new ServletException(message);
            }

        }

        return methodName;
    }

    /**
     * NEW: added by Jaap
     *
     * This is the first time the default Locale is used so build the reverse lookup Map.
     * Search for message keys in all configured MessageResources for
     * the current module.
     */
    protected Map initDefaultLookupMap(HttpServletRequest request) {
        Map lookupMap = new HashMap();
        this.keyMethodMap = this.getKeyMethodMap();

        ModuleConfig moduleConfig =
                (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);

        MessageResourcesConfig[] mrc = moduleConfig.findMessageResourcesConfigs();

        // Look through all module's MessageResources
        for (int i = 0; i < mrc.length; i++) {
            MessageResources resources = this.getResources(request, mrc[i].getKey());

            // Look for key in MessageResources
            Iterator iter = this.keyMethodMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String text = resources.getMessage(Locale.ENGLISH, key);

                // Found key and haven't added to Map yet, so add the text
                if ((text != null) && !lookupMap.containsKey(text)) {
                    lookupMap.put(text, key);
                }
            }
        }

        return lookupMap;
    }
}
