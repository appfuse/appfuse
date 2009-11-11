package org.appfuse.webapp.pages;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.appfuse.Constants;
import org.slf4j.Logger;
import org.springframework.security.ui.AbstractProcessingFilter;

/**
 * Login Page
 *
 * @author Serge Eby
 * @version $Id: Login.java 5 2008-08-30 09:59:21Z serge.eby $
 */
@IncludeJavaScriptLibrary("context:scripts/login.js")
public class Login extends BasePage {

    private static final String AUTH_FAILED = "error";
    private static final String SECURITY_URL = "/j_security_check";

    @Inject
    private Logger logger;

    @Inject
    private Request request;

    @Property
    @Inject
    @Path("context:images/iconWarning.gif")
    private Asset iconWarning;

    @Inject
    private Messages messages;

    @Inject
    private PageRenderLinkSource linker;

    @Inject
    private Context context;

    @Environmental
    private RenderSupport renderSupport;

    @Property
    private String errorMessage;

    void onActivate(String loginError) {
        if (AUTH_FAILED.equals(loginError)) {
            this.errorMessage = ((Exception) request
                    .getSession(true)
                    .getAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY))
                    .getMessage();
            logger.error(String.format("Error while attempting to login: %s",
                    errorMessage));
        }
    }

    String onPassivate() {
        return errorMessage;
    }

    void afterRender() {
        JSONObject params = new JSONObject();
        params.put("url", getUrl());
        params.put("passwordHintLink", getPasswordHintLink());
        params.put("requiredUsername", getRequiredUsernameError());
        params.put("requiredPassword", getRequiredPasswordError());

        renderSupport.addScript("initialize(%s);", params);

    }

    public String getSpringSecurityUrl() {
        return String.format("%s%s", request.getContextPath(), SECURITY_URL);
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

    public String getSignup() {
        String link = linker.createPageRenderLink("Signup").toAbsoluteURI();
        return MessageFormat.format(messages.get("login.signup"), link);
    }

    //~-- Javascript/JSON object helper methods
    private String getRequiredFieldError(String field) {
        return MessageFormat.format(messages.get("errors.required"), field);
    }

    private String getRequiredUsernameError() {
        return getRequiredFieldError(messages.get("label.username"));
    }

    private String getRequiredPasswordError() {
        return getRequiredFieldError(messages.get("label.password"));
    }

    private String getPasswordHintLink() {
        return linker.createPageRenderLink("PasswordHint").toAbsoluteURI();
    }

    private String getUrl() {
        return linker.createPageRenderLink("Login").toAbsoluteURI();
    }

    public String getCssTheme() {
        return context.getInitParameter(Constants.CSS_THEME);
    }
}
