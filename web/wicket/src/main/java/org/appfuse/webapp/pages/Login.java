package org.appfuse.webapp.pages;

import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.RequestUtils;
import org.appfuse.Constants;
import org.appfuse.webapp.AbstractWebPage;
import org.appfuse.webapp.pages.components.RequiredLabel;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Page for login to the system.
 *
 * @author Marcin Zajączkowski, 2010-09-02
 */
@MountPath(path = "login")
public class Login extends AbstractWebPage {

    private TextField<String> usernameField;
    private TextField<String> passwordField;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addLayoutColCssToHeader();
        add(createLoginJavaScriptHeaderContribution());
        add(createInitDataOnLoadBehavior());

        //TODO: MZA: Add login hint with: http://code.google.com/p/visural-wicket/

        Form<Void> loginForm = createLoginForm();
        add(loginForm);

        loginForm.add(createFeedbackPanel());
        createAndAddToLoginFormUsernameAndPasswordFields(loginForm);
        loginForm.add(createRememberMeGroup());
        loginForm.add(createSignupLabel());

    }

    private void createAndAddToLoginFormUsernameAndPasswordFields(Form<Void> loginForm) {
        loginForm.add(new RequiredLabel("usernameLabel", getString("label.username")));
        //Border to add red asterisk on error, FormComponentFeedbackIndicator could be used for something more.
        //TODO: MZA: Take a look at FormComponentFeedbackBorder markup (its wicket:body) as a hint for required label
        //TODO: MZA: How to change a background color of an input with validation error?
        loginForm.add(new FormComponentFeedbackBorder("border").add(
                (usernameField = new RequiredTextField<String>("username", Model.of("")))));
        loginForm.add(new RequiredLabel("passwordLabel", getString("label.password")));
        loginForm.add(passwordField = new PasswordTextField("password", Model.of("")));
    }

    private Form<Void> createLoginForm() {
        return new Form<Void>("loginForm") {
            @Override
            protected void onSubmit() {
                authenticateUser();
            }
        };
    }

    private FeedbackPanel createFeedbackPanel() {
        return new FeedbackPanel("feedback") {
            @Override
            protected String getCSSClass(final FeedbackMessage message) {
                return message.getLevelAsString().toLowerCase();
            }
        };
    }

    //TODO: MZA: Move to is needed somewhere else
    private void addLayoutColCssToHeader() {
        String cssTheme = (String)getValueForKeyFromConfigOrReturnNullIfNoConfig(Constants.CSS_THEME);
        if (cssTheme == null) {
            //TODO: MZA: Problem with return object to add (NPE) - is there any Null Object to add?
            log.warn("{} parameter not set in web.xml. Additional CSS can be missing.", Constants.CSS_THEME);
        } else {
            String layout1ColCss = "/styles/" + cssTheme + "/layout-1col.css";
            add(CSSPackageResource.getHeaderContribution(layout1ColCss));
        }
    }

    //TODO: MZA: Move to is needed somewhere else
    @SuppressWarnings("unchecked")
    private Object getValueForKeyFromConfigOrReturnNullIfNoConfig(String configProperty) {
        Map<String, Object> config = (Map<String, Object>)getServletContext().getAttribute(Constants.CONFIG);
        if (config == null) {
            log.warn("{} context attribute is null.", Constants.CONFIG);
            return null;
        } else {
            return config.get(configProperty);
        }
    }

    private HeaderContributor createLoginJavaScriptHeaderContribution() {
        String loginJs = "/scripts/login.js";
        return JavascriptPackageResource.getHeaderContribution(loginJs);
    }

    private AbstractBehavior createInitDataOnLoadBehavior() {
        return new AbstractBehavior() {
            @Override
            public void renderHead(IHeaderResponse response) {
                super.renderHead(response);
                response.renderOnLoadJavascript("initDataOnLoad()");
            }
        };
    }

    private WebMarkupContainer createRememberMeGroup() {
        WebMarkupContainer rememberMeGroup = new WebMarkupContainer("rememberMeGroup");
        rememberMeGroup.add(rememberMeCheckBox = new CheckBox("rememberMe", new Model<Boolean>(Boolean.FALSE)));
        //TODO: MZA: How to keep fields? As class property? Local variable? just assign to a parent?
        rememberMeGroup.add(new Label("rememberMeLabel", getString("login.rememberMe")));

        boolean isRememberMeEnabled = isRememberMeEnabled();
        rememberMeGroup.setVisible(isRememberMeEnabled);
        //TODO: MZA: How does RememberMe work? I don't see any cookie.
        return rememberMeGroup;
    }

    private boolean isRememberMeEnabled() {
        Boolean isRememberMeEnabled = (Boolean)getValueForKeyFromConfigOrReturnNullIfNoConfig("rememberMeEnabled");
        return isRememberMeEnabled != null ? isRememberMeEnabled : false;
    }

    private Label createSignupLabel() {
        String absoluteSignupLink = RequestUtils.toAbsolutePath(urlFor(Signup.class, null).toString());
        //TODO: MZA: There should be some better way to use URL inside a label (if not, make it an util method)
        String signupLabelText = new StringResourceModel("login.signup", this, null, new Object[] {
                absoluteSignupLink}).getString();
        Label signupLabel = new Label("signupLabel", signupLabelText);
        signupLabel.setEscapeModelStrings(false);
        return signupLabel;
    }

    private void authenticateUser() {
        //TODO: MZA: Why the first login user is redirected again to login page?
        AuthenticatedWebSession session = AuthenticatedWebSession.get();
        if (session.signIn(usernameField.getModelObject(), passwordField.getModelObject())) {
            setDefaultResponsePageIfNecessary();
        } else {
            error(getString("errors.password.mismatch"));
        }
    }

    private void setDefaultResponsePageIfNecessary() {
        if(!continueToOriginalDestination()) {
            setResponsePage(getApplication().getHomePage());
            setRedirect(true);
        }
    }
}
