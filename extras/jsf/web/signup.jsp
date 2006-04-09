<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="signup.title"/></title>
<content tag="heading"><fmt:message key="signup.heading"/></content>
<body id="signup"/>

<f:view>
<f:loadBundle var="text" basename="#{signupForm.bundleName}"/>

<p><fmt:message key="signup.message"/></p>

<div class="separator"></div>

<h:form id="signupForm" onsubmit="return validateSignupForm(this)">

<h:panelGrid columns="3" styleClass="detail" columnClasses="label">

    <h:outputLabel for="username" value="#{text['user.username']}"/>

    <h:inputText value="#{signupForm.user.username}" id="username" required="true">
        <v:commonsValidator type="required" arg="#{text['user.username']}"/>
    </h:inputText>
    <t:message for="username" styleClass="fieldError"/>

    <h:outputLabel for="password" value="#{text['user.password']}"/>

    <h:inputSecret value="#{signupForm.user.password}" id="password"
        size="40" redisplay="true" required="true">
        <v:commonsValidator type="required" arg="#{text['user.password']}"/>
    </h:inputSecret>
    <t:message for="password" styleClass="fieldError"/>

    <h:outputLabel for="confirmPassword" value="#{text['user.confirmPassword']}"/>

    <h:inputSecret value="#{signupForm.user.confirmPassword}" id="confirmPassword"
        size="40" redisplay="true" required="true">
        <v:commonsValidator type="required" arg="#{text['user.confirmPassword']}"/>
        <t:validateEqual for="password"/>
    </h:inputSecret>
    <t:message for="confirmPassword" styleClass="fieldError"/>

    <h:outputLabel for="firstName" value="#{text['user.firstName']}"/>

    <h:inputText id="firstName" value="#{signupForm.user.firstName}" maxlength="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.firstName']}"/>
    </h:inputText>
    <t:message for="firstName" styleClass="fieldError"/>

    <h:outputLabel for="lastName" value="#{text['user.lastName']}"/>

    <h:inputText value="#{signupForm.user.lastName}" id="lastName" maxlength="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.lastName']}"/>
    </h:inputText>
    <t:message for="lastName" styleClass="fieldError"/>

    <h:outputLabel for="address" value="#{text['user.address.address']}"/>

    <h:inputText value="#{signupForm.user.address.address}" id="address" size="50"/>
    <t:message for="address" styleClass="fieldError"/>

    <h:outputLabel for="city" value="#{text['user.address.city']}"/>

    <h:inputText value="#{signupForm.user.address.city}" id="city" size="40" required="true">
        <v:commonsValidator type="required" arg="#{text['user.address.city']}"/>
    </h:inputText>
    <t:message for="city" styleClass="fieldError"/>

    <h:outputLabel for="province" value="#{text['user.address.province']}"/>

    <h:inputText value="#{signupForm.user.address.province}" id="province" size="40" required="true">
        <v:commonsValidator type="required" arg="#{text['user.address.province']}"/>
    </h:inputText>
    <t:message for="province" styleClass="fieldError"/>

    <h:outputLabel for="country" value="#{text['user.address.country']}"/>

    <h:selectOneMenu value="#{signupForm.country}" id="country" required="true">
        <f:selectItems value="#{signupForm.countries}"/>
        <v:commonsValidator type="required" arg="#{text['user.address.country']}"/>
    </h:selectOneMenu>
    <t:message for="country" styleClass="fieldError"/>

    <h:outputLabel for="postalCode" value="#{text['user.address.postalCode']}"/>

    <h:inputText value="#{signupForm.user.address.postalCode}" id="postalCode" size="10" required="true">
        <v:commonsValidator type="required" arg="#{text['user.address.postalCode']}"/>
        <t:validateRegExpr pattern="^\d{5}\d*$"/>
    </h:inputText>
    <t:message for="postalCode" styleClass="fieldError"/>

    <h:outputLabel for="email" value="#{text['user.email']}"/>

    <h:inputText value="#{signupForm.user.email}" id="email" size="50" required="true">
        <f:validator validatorId="org.apache.myfaces.validator.Email"/>
        <v:commonsValidator type="required" arg="#{text['user.email']}"/>
    </h:inputText>
    <t:message for="email" styleClass="fieldError"/>

    <h:outputLabel for="phoneNumber" value="#{text['user.phoneNumber']}"/>

    <h:inputText value="#{signupForm.user.phoneNumber}" id="phoneNumber">
        <t:validateRegExpr pattern="^\(?(\d{3})\)?[-| ]?(\d{3})[-| ]?(\d{4})$"/>
    </h:inputText>
    <t:message for="phoneNumber" styleClass="fieldError"/>

    <h:outputLabel for="website" value="#{text['user.website']}"/>

    <h:inputText value="#{signupForm.user.website}" id="website" size="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.website']}"/>
    </h:inputText>
    <t:message for="website" styleClass="fieldError"/>

    <h:outputLabel for="passwordHint" value="#{text['user.passwordHint']}"/>

    <h:inputText value="#{signupForm.user.passwordHint}" id="passwordHint" size="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.passwordHint']}"/>
    </h:inputText>
    <t:message for="passwordHint" styleClass="fieldError"/>

    <h:inputHidden value=""/>
    
    <h:panelGroup styleClass="buttonBar">
        <h:commandButton value="#{text['button.register']}" action="#{signupForm.save}" 
            id="save" styleClass="button"/>
    
        <h:commandButton value="#{text['button.cancel']}" action="cancel" immediate="true"  
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
    
    <h:inputHidden value=""/>
    
</h:panelGrid>
</h:form>

<v:validatorScript functionName="validateSignupForm"/>

<script type="text/javascript">
    Form.focusFirstElement(document.forms["signupForm"]);
</script>

</f:view>
