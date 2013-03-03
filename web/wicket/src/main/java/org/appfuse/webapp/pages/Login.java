package org.appfuse.webapp.pages;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.appfuse.Constants;
import org.appfuse.webapp.AbstractWebPage;
import org.appfuse.webapp.pages.components.PlaceholderBehavior;
import org.appfuse.webapp.pages.components.RequiredBehavior;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Map;

/**
 * Page for login to the system.
 *
 * @author Marcin Zajączkowski, 2010-09-02
 */
@MountPath("login")
public class Login extends AbstractWebPage {

    private TextField<String> usernameField;
    private TextField<String> passwordField;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createPageTitleTag("login.title"));

        //TODO: MZA: Add login hint with: http://code.google.com/p/visural-wicket/

        Form<Void> loginForm = createLoginForm();
        add(loginForm);

        loginForm.add(createFeedbackPanel());
        loginForm.add(createPageHeading("login.heading"));
        createAndAddToLoginFormUsernameAndPasswordFields(loginForm);
        loginForm.add(createRememberMeGroup());
        add(createSignupLabel());

    }

    private void createAndAddToLoginFormUsernameAndPasswordFields(Form<Void> loginForm) {
        //Border to add red asterisk on error, FormComponentFeedbackIndicator could be used for something more.
        //TODO: MZA: Take a look at FormComponentFeedbackBorder markup (its wicket:body) as a hint for required label
        //TODO: MZA: How to change a background color of an input with validation error?
        loginForm.add(new FormComponentFeedbackBorder("border").add(
                (usernameField = new RequiredTextField<String>("username", Model.of("")))));
        usernameField.add(new PlaceholderBehavior(getString("label.username")));
        usernameField.add(new RequiredBehavior());
        loginForm.add(passwordField = new PasswordTextField("password", Model.of("")));
        passwordField.add(new PlaceholderBehavior(getString("label.password")));
        passwordField.add(new RequiredBehavior());
    }

    private Form<Void> createLoginForm() {
        return new Form<Void>("loginForm") {
            @Override
            protected void onSubmit() {
                authenticateUser();
            }
        };
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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        addLoginJavaScriptToResponse(response);
        addInitDataOnLoadJavaScriptToResponse(response);
    }

    private void addLoginJavaScriptToResponse(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forUrl("scripts/login.js"));
    }

    private void addInitDataOnLoadJavaScriptToResponse(IHeaderResponse response) {
        response.render(OnLoadHeaderItem.forScript("initDataOnLoad()"));
    }

    private WebMarkupContainer createRememberMeGroup() {
        WebMarkupContainer rememberMeGroup = new WebMarkupContainer("rememberMeGroup");
        rememberMeGroup.add(createRememberMeCheckboxInsideLabel());

        boolean isRememberMeEnabled = isRememberMeEnabled();
        rememberMeGroup.setVisible(isRememberMeEnabled);
        //TODO: MZA: How does RememberMe work? I don't see any cookie.
        return rememberMeGroup;
    }

    private FormComponentLabel createRememberMeCheckboxInsideLabel() {
        //TODO: MZA: A trick to have an input component inside a label - pack into a new component if needed somewhere else
        // https://cwiki.apache.org/WICKET/form-control-labels.html
        //TODO: MZA: How to keep fields? As class property? Local variable? just assign to a parent?
        rememberMeCheckBox = new CheckBox("rememberMe", new Model<Boolean>(Boolean.FALSE));
        FormComponentLabel rememberMeLabel = new FormComponentLabel("rememberMeLabel", rememberMeCheckBox);
        Label rememberMeLabelSpan = new Label("rememberMeLabelSpan", getString("login.rememberMe"));
        rememberMeLabelSpan.setRenderBodyOnly(true);
        rememberMeLabel.add(rememberMeLabelSpan);
        rememberMeLabel.add(rememberMeCheckBox);
        return rememberMeLabel;
    }

    private boolean isRememberMeEnabled() {
        Boolean isRememberMeEnabled = (Boolean)getValueForKeyFromConfigOrReturnNullIfNoConfig("rememberMeEnabled");
        return isRememberMeEnabled != null ? isRememberMeEnabled : false;
    }

    private Label createSignupLabel() {
        String absoluteSignupLink = RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(urlFor(Signup.class, null).toString()));
        //TODO: MZA: There should be some better way to use URL inside a label (if not, make it an util method)
        String signupLabelText = new StringResourceModel("login.signup", this, null, new Object[] {
                absoluteSignupLink}).getString();
        Label signupLabel = new Label("signupLabel", signupLabelText);
        signupLabel.setEscapeModelStrings(false);
        return signupLabel;
    }

    private void authenticateUser() {
        AuthenticatedWebSession session = AuthenticatedWebSession.get();
        if (session.signIn(usernameField.getModelObject(), passwordField.getModelObject())) {
            setDefaultResponsePageIfNecessary();
        } else {
            error(getString("errors.password.mismatch"));
        }
    }

    private void setDefaultResponsePageIfNecessary() {
        continueToOriginalDestination();
        setResponsePage(getApplication().getHomePage());
    }
}
