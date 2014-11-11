package org.appfuse.webapp.pages;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.appfuse.Constants;
import org.appfuse.webapp.AppFuseSymbolConstants;
import org.slf4j.Logger;
import org.springframework.security.web.WebAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Login Page
 *
 * @author Serge Eby
 * @version $Id: Login.java 5 2008-08-30 09:59:21Z serge.eby $
 */

@Import(library = {"plugins/jquery.cookie.js"}, module = "app/login")
public class Login {

    @Inject
    private Logger logger;

    @Inject
    @Symbol(AppFuseSymbolConstants.SECURITY_URL)
    private String securityUrl;

    @Inject
    @Symbol(SymbolConstants.CONTEXT_PATH)
    private String contextPath;

    @Inject
    private Request request;

    @Inject
    private Messages messages;

    @Inject
    private AlertManager alertManager;

    @Inject
    private PageRenderLinkSource pageRendererLinkSource;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @Property
    private String errorMessage;

    @Inject
    private Context context;

    @Log
    void onActivate(String loginError) {
        if ("error".equals(loginError)) {
            this.errorMessage = ((Exception) request
                .getSession(true)
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION))
                .getMessage();
            logger.error(String.format("Error while attempting to login: %s",
                errorMessage));
        }
    }

    String onPassivate() {
        return errorMessage;
    }

    void afterRender() {
        JSONObject spec = new JSONObject();

        String requiredUsernameError = messages.format("errors.required",
            messages.get("label.username"));
        String requiredPasswordError = messages.format("errors.required",
            messages.get("label.password"));

        spec.put("passwordHintUrl", createLink(PasswordHint.class))
            .put("passwordResetUrl", createLink(PasswordRecoveryToken.class))
            .put("requiredUsername", requiredUsernameError)
            .put("requiredPassword", requiredPasswordError);

        // For some reason, the line below causes JavaScript to fail to load (loading screen)
        //javascriptSupport.require("app/login").invoke("init").with(spec);
    }

    public String getSpringSecurityUrl() {
        return contextPath + securityUrl;
    }

    void cleanupRender() {
        this.errorMessage = null;
    }

    @SuppressWarnings("unchecked")
    public boolean isRememberMeEnabled() {
        try {
            Map config = (HashMap) context.getAttribute(Constants.CONFIG);
            if (config != null) {
                return (config.get("rememberMeEnabled") != null);
            }
        } catch (UnsupportedOperationException uoe) {
            // only happens in tests
            // getAttribute() is not supported for ContextForPageTester
            logger.warn(uoe.getMessage());
        }
        return false;
    }

    public String getSignupLink() {
        String link = createLink(Signup.class);
        return messages.format("login.signup", link);
    }

    private String createLink(Class clazz) {
        return pageRendererLinkSource.createPageRenderLink(clazz).toAbsoluteURI();
    }
}
