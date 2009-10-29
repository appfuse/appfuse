package org.appfuse.webapp.components;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.appfuse.model.User;
import org.appfuse.webapp.services.ServiceFacade;

/**
 * Generic form for User manipulation
 *
 * @author Serge Eby
 * @version $Id: UserForm.java 5 2008-08-30 09:59:21Z serge.eby $
 */
@SupportsInformalParameters
public class UserForm implements ClientElement {

    @Parameter(required = true)
    @Property
    private User user;

    @Parameter
    @Property
    private boolean signup = false;

    @Parameter(value = "message:button.save", defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String submitLabel;

    @Parameter(allowNull = true)
    private String from;

    @Parameter
    @Property
    private boolean cookieLogin = true;

    @Parameter
    private boolean clientValidation = false;

    @Parameter(allowNull = true)
    @Property
    private List<String> selectedRoles;

    @Parameter(allowNull = true)
    @Property
    private List<String> userRoles;

    @Inject
    private ComponentDefaultProvider defaultProvider;

    @Inject
    private ComponentResources resources;

    @Inject
    private ServiceFacade serviceFacade;

    @Component(parameters = {"clientValidation=inherit:clientValidation"})
    private Form form;

    @Component(id = "username", parameters = {"value=user.username", "validate=required", "label=message:user.username"})
    private TextField usernameField;

    @Component(id = "password", parameters = {"value=user.password", "validate=required"})
    private HashedPasswordField passwordField;

    @Component(id = "confirmPassword", parameters = {"value=user.confirmPassword", "validate=required"})
    private HashedPasswordField confirmPasswordField;

    @Component(id = "passwordHint", parameters = {"value=user.passwordHint", "validate=required"})
    private TextField passwordHintField;

    @Component(id = "firstName", parameters = {"value=user.firstName", "validate=required"})
    private TextField firstNameField;

    @Component(id = "lastName", parameters = {"value=user.lastName", "validate=required"})
    private TextField lastNameField;

    @Component(id = "email", parameters = {"value=user.email", "validate=required"})
    private TextField emailField;

    @Component(id = "phoneNumber", parameters = {"value=user.phoneNumber", "validate=minlength=11"})
    private TextField phoneNumberField;

    @Component(id = "website", parameters = {"value=user.website", "validate=required"})
    private TextField websiteField;

    @Component(id = "address", parameters = {"value=user.address.address"})
    private TextField addressField;

    @Component(id = "city", parameters = {"value=user.address.city", "validate=required"})
    private TextField cityField;

    @Component(id = "state", parameters = {"value=user.address.province", "validate=required"})
    private TextField stateField;

    @Component(id = "postalCode", parameters = {"value=user.address.postalCode", "validate=required"})
    private TextField postalCodeField;

    @Component(id = "country",
            parameters = {"value=user.address.country",
                    "model=countryModel",
                    "encoder=encoder",
                    "validate=required",
                    "blankoption=always"})
    private Select countryField;

    @Component(parameters = {"event=cancel"})
    private EventLink cancelTop, cancelBottom;

//      NOTE: Wrapping a button in a eventlink results into 2 attempts to
//        delete the user. We will use a submit component instead
//        @Component(parameters = { "event=delete" })
//        private EventLink deleteTop, deleteBottom;

    @Component(id = "deleteTop")
    private Submit deleteTop;

    @Component(id = "deleteBottom")
    private Submit deleteBottom;

    public ValueEncoder<String> getEncoder() {
        return new StringValueEncoder();
    }

    public Field getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public Form getForm() {
        return form;
    }

    public String getClientId() {
        return form.getClientId();
    }

    Binding defaultObject() {
        return defaultProvider.defaultBinding("user", resources);
    }

    public void clearErrors() {
        form.clearErrors();
    }

    public boolean getHasErrors() {
        return form.getHasErrors();
    }

    public boolean isValid() {
        return form.isValid();
    }

    public void recordError(Field field, String errorMessage) {
        form.recordError(field, errorMessage);
    }

    public void recordError(String errorMessage) {
        form.recordError(errorMessage);
    }

    @Cached
    public Map<String, String> getCountryModel() {
        return serviceFacade.getAvailableCountries();
    }

    @Cached
    public List<String> getRoleModel() {
        return serviceFacade.getAvailableRoles();
    }

    public boolean isUserVersionNull() {
        return (user != null ? user.getVersion() == null : true);
    }

    public boolean isFromList() {
        return "list".equals(from);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}

