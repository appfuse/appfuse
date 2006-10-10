<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
    <meta name="menu" content="UserMenu"/>
</head>

<f:view>
<f:loadBundle var="text" basename="#{userForm.bundleName}"/>

<div class="separator"></div>

<h:form id="userForm" onsubmit="return validateUserForm(this)">
<h:inputHidden value="#{userForm.user.id}" id="id"/>
<h:inputHidden value="#{userForm.user.version}" id="version"/>

<%-- Original password as hidden field so we can compare and encrypt --%>
<h:inputHidden value="#{userForm.user.password}" id="originalPassword"/>
<input type="hidden" name="from" value="<h:outputText value="#{userForm.from}"/>" />

<c:if test="${cookieLogin == 'true'}">
    <h:inputHidden value="#{userForm.user.password}" id="password"/>
    <h:inputHidden value="#{userForm.user.confirmPassword}" id="confirmPassword"/>
</c:if>

<c:if test="${empty userForm.user.username}">
<input type="hidden" name="encryptPass" value="true" />
</c:if>

<%-- Must be set outside of panelGrid, or gets output as a cell --%>
<c:set var="addText"><h:outputText value="#{text['button.add']}"/></c:set>

<h:panelGrid columns="3">

    <h:panelGroup styleClass="buttonBar right">
        <h:commandButton value="#{text['button.save']}" action="#{userForm.save}" styleClass="button"/>
        <c:if test="${param.from == 'list'}">
        <h:commandButton value="#{text['button.delete']}" action="#{userForm.delete}"
            styleClass="button" onclick="bCancel=true; return confirmDelete('User')"/>
        </c:if>
        <h:commandButton value="#{text['button.cancel']}" action="#{userForm.cancel}" immediate="true"
            styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
    <h:outputText/><h:outputText/>

    <h:panelGroup styleClass="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <h:outputText value="#{text['userProfile.admin.message']}"/>
            </c:when>
            <c:otherwise><h:outputText value="#{text['userProfile.message']}"/></c:otherwise>
        </c:choose>
    </h:panelGroup>
    <h:outputText/><h:outputText/>

    <h:outputLabel for="username" styleClass="desc" value="#{text['user.username']}"/>
    <t:message for="username" styleClass="fieldError"/>
    <h:inputText value="#{userForm.user.username}" id="username" required="true" styleClass="text large">
        <v:commonsValidator type="required" arg="#{text['user.username']}"/>
    </h:inputText>

    <c:if test="${cookieLogin != 'true'}">
    <h:panelGroup>
        <t:htmlTag value="div">
            <t:htmlTag value="div" styleClass="left">
                <h:outputLabel for="password" value="#{text['user.password']}" styleClass="desc"/>
                <t:message for="password" styleClass="fieldError"/>
                <h:inputSecret value="#{userForm.user.password}" id="password"
                    redisplay="true" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.password']}"/>
                </h:inputSecret>
            </t:htmlTag>
            <t:htmlTag value="div">
                <h:outputLabel for="confirmPassword" value="#{text['user.confirmPassword']}" styleClass="desc"/>
                <t:message for="confirmPassword" styleClass="fieldError"/>
                <h:inputSecret value="#{userForm.user.confirmPassword}" id="confirmPassword"
                    redisplay="true" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.confirmPassword']}"/>
                    <t:validateEqual for="password"/>
                </h:inputSecret>
            </t:htmlTag>
        </t:htmlTag>
    </h:panelGroup>
    <h:outputText/><h:outputText/>
    </c:if>

    <h:outputLabel for="passwordHint" value="#{text['user.passwordHint']}" styleClass="desc"/>
    <t:message for="passwordHint" styleClass="fieldError"/>
    <h:inputText value="#{userForm.user.passwordHint}" id="passwordHint" required="true" styleClass="text large">
        <v:commonsValidator type="required" arg="#{text['user.passwordHint']}"/>
    </h:inputText>

    <h:panelGroup>
        <t:htmlTag value="div">
            <t:htmlTag value="div" styleClass="left">
                <h:outputLabel for="firstName" value="#{text['user.firstName']}" styleClass="desc"/>
                <t:message for="firstName" styleClass="fieldError"/>
                <h:inputText id="firstName" value="#{userForm.user.firstName}" maxlength="50" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.firstName']}"/>
                </h:inputText>
            </t:htmlTag>
            <t:htmlTag value="div">
                <h:outputLabel for="lastName" value="#{text['user.lastName']}" styleClass="desc"/>
                <t:message for="lastName" styleClass="fieldError"/>
                <h:inputText value="#{userForm.user.lastName}" id="lastName" maxlength="50" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.lastName']}"/>
                </h:inputText>
            </t:htmlTag>
        </t:htmlTag>
    </h:panelGroup>
    <h:outputText/><h:outputText/>

    <h:panelGroup>
        <t:htmlTag value="div">
            <t:htmlTag value="div" styleClass="left">
                <h:outputLabel for="email" value="#{text['user.email']}" styleClass="desc"/>
                <t:message for="email" styleClass="fieldError"/>
                <h:inputText value="#{userForm.user.email}" id="email" required="true" styleClass="text medium">
                    <f:validator validatorId="org.apache.myfaces.validator.Email"/>
                    <v:commonsValidator type="required" arg="#{text['user.email']}"/>
                    <v:commonsValidator type="email" arg="#{text['user.email']}"/>
                </h:inputText>
            </t:htmlTag>
            <t:htmlTag value="div">
                <h:outputLabel for="phoneNumber" value="#{text['user.phoneNumber']}" styleClass="desc"/>
                <t:message for="phoneNumber" styleClass="fieldError"/>
                <h:inputText value="#{userForm.user.phoneNumber}" id="phoneNumber" styleClass="text medium">
                    <t:validateRegExpr pattern="^\(?(\d{3})\)?[-| ]?(\d{3})[-| ]?(\d{4})$"/>
                </h:inputText>
            </t:htmlTag>
        </t:htmlTag>
    </h:panelGroup>
    <h:outputText/><h:outputText/>

    <h:outputLabel for="website" value="#{text['user.website']}" styleClass="desc"/>
    <t:message for="website" styleClass="fieldError"/>
    <h:inputText value="#{userForm.user.website}" id="website" required="true" styleClass="text large">
        <v:commonsValidator type="required" arg="#{text['user.website']}"/>
    </h:inputText>

    <h:panelGroup>
        <t:htmlTag value="label" styleClass="desc"><h:outputText value="#{text['user.address.address']}"/></t:htmlTag>
        <t:htmlTag value="div" styleClass="group">
            <t:htmlTag value="div">
                <h:inputText value="#{userForm.user.address.address}" id="address" styleClass="text large"/>
                <t:message for="address" styleClass="fieldError"/>
                <t:htmlTag value="p"><h:outputLabel for="address" value="#{text['user.address.address']}"/></t:htmlTag>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="left">
                <h:inputText value="#{userForm.user.address.city}" id="city" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.address.city']}"/>
                </h:inputText>
                <t:message for="city" styleClass="fieldError"/>
                <t:htmlTag value="p"><h:outputLabel for="city" value="#{text['user.address.city']}"/></t:htmlTag>
            </t:htmlTag>
            <t:htmlTag value="div">
                <h:inputText value="#{userForm.user.address.province}" id="province" size="2" required="true" styleClass="text state">
                    <v:commonsValidator type="required" arg="#{text['user.address.province']}"/>
                </h:inputText>
                <t:message for="province" styleClass="fieldError"/>
                <t:htmlTag value="p"><h:outputLabel for="province" value="#{text['user.address.province']}"/></t:htmlTag>
            </t:htmlTag>
            <t:htmlTag value="div" styleClass="left">
                <h:inputText value="#{userForm.user.address.postalCode}" id="postalCode" required="true" styleClass="text zip">
                    <v:commonsValidator type="required" arg="#{text['user.address.postalCode']}"/>
                    <t:validateRegExpr pattern="^\d{5}\d*$"/>
                </h:inputText>
                <t:message for="postalCode" styleClass="fieldError"/>
                <t:htmlTag value="p"><h:outputLabel for="postalCode" value="#{text['user.address.postalCode']}"/></t:htmlTag>
            </t:htmlTag>
            <t:htmlTag value="div">
                <h:selectOneMenu value="#{userForm.country}" id="country" required="true" styleClass="select">
                    <f:selectItems value="#{userForm.countries}"/>
                    <v:commonsValidator type="required" arg="#{text['user.address.country']}"/>
                </h:selectOneMenu>
                <t:message for="country" styleClass="fieldError"/>
                <t:htmlTag value="p"><h:outputLabel for="country" value="#{text['user.address.country']}"/></t:htmlTag>
            </t:htmlTag>
        </t:htmlTag>
    </h:panelGroup>
    <h:outputText/><h:outputText/>

<c:choose>
    <c:when test="${param.from == 'list' or param['editUser:add'] == addText}">
    <h:panelGroup>
        <t:htmlTag value="fieldset">
            <t:htmlTag value="legend"><h:outputText value="#{text['userProfile.accountSettings']}"/></t:htmlTag>
            <h:selectBooleanCheckbox value="#{userForm.user.enabled}" id="enabled" styleClass="checkbox"/>
            <h:outputLabel for="enabled" styleClass="choice" value="#{text['user.enabled']}"/>

            <h:selectBooleanCheckbox value="#{userForm.user.accountExpired}" id="accountExpired" styleClass="checkbox"/>
            <h:outputLabel for="accountExpired" styleClass="choice" value="#{text['user.accountExpired']}"/>

            <h:selectBooleanCheckbox value="#{userForm.user.accountLocked}" id="accountLocked" styleClass="checkbox"/>
            <h:outputLabel for="accountLocked" styleClass="choice" value="#{text['user.accountLocked']}"/>

            <h:selectBooleanCheckbox value="#{userForm.user.credentialsExpired}" id="credentialsExpired" styleClass="checkbox"/>
            <h:outputLabel for="credentialsExpired" styleClass="choice" value="#{text['user.credentialsExpired']}"/>
        </t:htmlTag>
    </h:panelGroup>
    <h:outputText/><h:outputText/>
    <h:panelGroup>
        <t:htmlTag value="fieldset">
            <t:htmlTag value="legend"><h:outputText value="#{text['userProfile.assignRoles']}"/></t:htmlTag>
            <h:selectManyCheckbox value="#{userForm.userRoles}" id="userRoles">
                <f:selectItems value="#{userForm.availableRoles}"/>
            </h:selectManyCheckbox>
        </t:htmlTag>
    </h:panelGroup>
    <h:outputText/><h:outputText/>
    </c:when>
    <c:otherwise>
    <h:panelGroup>
        <t:htmlTag value="strong"><h:outputText value="#{text['user.roles']}"/><f:verbatim>:</f:verbatim></t:htmlTag>
        <f:verbatim>
        <c:forEach var="role" items="${userForm.userRoles}" varStatus="status">
            <c:out value="${role}"/><c:if test="${!status.last}">,</c:if>
            <input type="hidden" name="userForm:userRoles" value="<c:out value="${role}"/>" />
        </c:forEach>
        </f:verbatim>
        <h:inputHidden value="#{userForm.user.enabled}"/>
        <h:inputHidden value="#{userForm.user.accountExpired}"/>
        <h:inputHidden value="#{userForm.user.accountLocked}"/>
        <h:inputHidden value="#{userForm.user.credentialsExpired}"/>
    </h:panelGroup>
    <h:outputText/><h:outputText/>
    </c:otherwise>
</c:choose>

    <h:panelGroup styleClass="buttonBar bottom">
        <h:commandButton value="#{text['button.save']}" action="#{userForm.save}" styleClass="button"/>
        <c:if test="${param.from == 'list'}">
        <h:commandButton value="#{text['button.delete']}" action="#{userForm.delete}"
            styleClass="button" onclick="bCancel=true; return confirmDelete('User')"/>
        </c:if>
        <h:commandButton value="#{text['button.cancel']}" action="#{userForm.cancel}" immediate="true"
            styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
    <h:outputText/><h:outputText/>
</h:panelGrid>
</h:form>

<v:validatorScript functionName="validateUserForm"/>
<script type="text/javascript">
    Form.focusFirstElement($('userForm'));
    highlightFormElements();
</script>

</f:view>
