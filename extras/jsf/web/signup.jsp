<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="signup.title"/></title>
<content tag="heading"><fmt:message key="signup.heading"/></content>
<body id="signup"/>

<f:view>
<f:loadBundle var="text" basename="#{signupForm.bundleName}"/>

<div class="separator"></div>

<h:form id="signupForm" onsubmit="return validateSignupForm(this)">
<ul>
    <li class="info">
        <fmt:message key="signup.message"/>
    </li>
    <li>
        <h:outputLabel for="username" value="#{text['user.username']}" styleClass="desc"/>
        <t:message for="username" styleClass="fieldError"/>
        <h:inputText value="#{signupForm.user.username}" id="username" required="true" styleClass="text large">
            <v:commonsValidator type="required" arg="#{text['user.username']}"/>
        </h:inputText>
    </li>
    <li>
        <div>
            <div class="left">
                <h:outputLabel for="password" value="#{text['user.password']}" styleClass="desc"/>
                <t:message for="password" styleClass="fieldError"/>
                <h:inputSecret value="#{signupForm.user.password}" id="password"
                    redisplay="true" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.password']}"/>
                </h:inputSecret>
            </div>
            <div>
                <h:outputLabel for="confirmPassword" value="#{text['user.confirmPassword']}" styleClass="desc"/>
                <t:message for="confirmPassword" styleClass="fieldError"/>
                <h:inputSecret value="#{signupForm.user.confirmPassword}" id="confirmPassword"
                    redisplay="true" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.confirmPassword']}"/>
                    <t:validateEqual for="password"/>
                </h:inputSecret>
            </div>
        </div>
    </li>
    <li>
        <h:outputLabel for="passwordHint" value="#{text['user.passwordHint']}" styleClass="desc"/>
        <t:message for="passwordHint" styleClass="fieldError"/>
        <h:inputText value="#{signupForm.user.passwordHint}" id="passwordHint" required="true" styleClass="text large">
            <v:commonsValidator type="required" arg="#{text['user.passwordHint']}"/>
        </h:inputText>
    </li>
    <li>
        <div>
            <div class="left">
                <h:outputLabel for="firstName" value="#{text['user.firstName']}" styleClass="desc"/>
                <t:message for="firstName" styleClass="fieldError"/>
                <h:inputText id="firstName" value="#{signupForm.user.firstName}" maxlength="50" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.firstName']}"/>
                </h:inputText>
            </div>
            <div>
                <h:outputLabel for="lastName" value="#{text['user.lastName']}" styleClass="desc"/>
                <t:message for="lastName" styleClass="fieldError"/>
                <h:inputText value="#{signupForm.user.lastName}" id="lastName" maxlength="50" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.lastName']}"/>
                </h:inputText>
            </div>
        </div>
    </li>
    <li>
        <div>
            <div class="left">
                <h:outputLabel for="email" value="#{text['user.email']}" styleClass="desc"/>
                <t:message for="email" styleClass="fieldError"/>
                <h:inputText value="#{signupForm.user.email}" id="email" size="50" required="true" styleClass="text medium">
                    <f:validator validatorId="org.apache.myfaces.validator.Email"/>
                    <v:commonsValidator type="required" arg="#{text['user.email']}"/>
                    <v:commonsValidator type="email" arg="#{text['user.email']}"/>
                </h:inputText>
            </div>
            <div>
                <h:outputLabel for="phoneNumber" value="#{text['user.phoneNumber']}" styleClass="desc"/>
                <t:message for="phoneNumber" styleClass="fieldError"/>
                <h:inputText value="#{signupForm.user.phoneNumber}" id="phoneNumber" styleClass="text medium">
                    <t:validateRegExpr pattern="^\(?(\d{3})\)?[-| ]?(\d{3})[-| ]?(\d{4})$"/>
                </h:inputText>
            </div>
        </div>
    </li>
    <li>
        <h:outputLabel for="website" value="#{text['user.website']}" styleClass="desc"/>
        <t:message for="website" styleClass="fieldError"/>
        <h:inputText value="#{signupForm.user.website}" id="website" size="50" required="true" styleClass="text large">
            <v:commonsValidator type="required" arg="#{text['user.website']}"/>
        </h:inputText>
    </li>
    <li>
        <label class="desc"><fmt:message key="user.address.address"/></label>
        <div class="group">
            <div>
                <h:inputText value="#{signupForm.user.address.address}" id="address" size="50" styleClass="text large"/>
                <t:message for="address" styleClass="fieldError"/>
                <p><h:outputLabel for="address" value="#{text['user.address.address']}"/></p>
            </div>
            <div class="left">
                <h:inputText value="#{signupForm.user.address.city}" id="city" size="40" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.address.city']}"/>
                </h:inputText>
                <t:message for="city" styleClass="fieldError"/>
                <p><h:outputLabel for="city" value="#{text['user.address.city']}"/></p>
            </div>
            <div>
                <h:inputText value="#{signupForm.user.address.province}" id="province" size="2" required="true" styleClass="text state">
                    <v:commonsValidator type="required" arg="#{text['user.address.province']}"/>
                </h:inputText>
                <t:message for="province" styleClass="fieldError"/>
                <p><h:outputLabel for="province" value="#{text['user.address.province']}"/></p>
            </div>
            <div class="left">
                <h:inputText value="#{signupForm.user.address.postalCode}" id="postalCode" size="10" required="true" styleClass="text zip">
                    <v:commonsValidator type="required" arg="#{text['user.address.postalCode']}"/>
                    <t:validateRegExpr pattern="^\d{5}\d*$"/>
                </h:inputText>
                <t:message for="postalCode" styleClass="fieldError"/>
                <p><h:outputLabel for="postalCode" value="#{text['user.address.postalCode']}"/></p>
            </div>
            <div>
                <h:selectOneMenu value="#{signupForm.country}" id="country" required="true" styleClass="select">
                    <f:selectItems value="#{signupForm.countries}"/>
                    <v:commonsValidator type="required" arg="#{text['user.address.country']}"/>
                </h:selectOneMenu>
                <t:message for="country" styleClass="fieldError"/>
                <p><h:outputLabel for="country" value="#{text['user.address.country']}"/></p>
            </div>
        </div>
    </li>
    <li class="buttonBar bottom">
        <h:commandButton value="#{text['button.register']}" action="#{signupForm.save}" 
            id="save" styleClass="button"/>
    
        <h:commandButton value="#{text['button.cancel']}" action="cancel" immediate="true"  
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </li>
</ul>
</h:form>

<v:validatorScript functionName="validateSignupForm"/>

<script type="text/javascript">
    Form.focusFirstElement($('signupForm'));
</script>

</f:view>
