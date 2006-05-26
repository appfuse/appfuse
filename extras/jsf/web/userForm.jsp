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

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <h:commandButton value="#{text['button.save']}" action="#{userForm.save}"
                id="save" styleClass="button"/>

            <c:if test="${param.from == 'list'}">
            <h:commandButton value="#{text['button.delete']}" action="#{userForm.delete}"
                id="delete" styleClass="button" onclick="bCancel=false; return confirmDelete('User')"/>
            </c:if>

            <h:commandButton value="#{text['button.cancel']}" action="#{userForm.cancel}" immediate="true"
                id="cancel" styleClass="button" onclick="bCancel=true"/>
        </c:set>
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <p><fmt:message key="userProfile.admin.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="userProfile.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>
    <li>
        <h:outputLabel for="username" value="#{text['user.username']}" styleClass="desc"/>
        <t:message for="username" styleClass="fieldError"/>
        <h:inputText value="#{userForm.user.username}" id="username" required="true" styleClass="text large">
            <v:commonsValidator type="required" arg="#{text['user.username']}"/>
        </h:inputText>
    </li>
    <c:if test="${cookieLogin != 'true'}">
    <li>
        <div>
            <div class="left">
                <h:outputLabel for="password" value="#{text['user.password']}" styleClass="desc"/>
                <t:message for="password" styleClass="fieldError"/>
                <h:inputSecret value="#{userForm.user.password}" id="password"
                    redisplay="true" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.password']}"/>
                </h:inputSecret>
            </div>
            <div>
                <h:outputLabel for="confirmPassword" value="#{text['user.confirmPassword']}" styleClass="desc"/>
                <t:message for="confirmPassword" styleClass="fieldError"/>
                <h:inputSecret value="#{userForm.user.confirmPassword}" id="confirmPassword"
                    redisplay="true" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.confirmPassword']}"/>
                    <t:validateEqual for="password"/>
                </h:inputSecret>
            </div>
        </div>
    </li>
    </c:if>
    <li>
        <h:outputLabel for="passwordHint" value="#{text['user.passwordHint']}" styleClass="desc"/>
        <t:message for="passwordHint" styleClass="fieldError"/>
        <h:inputText value="#{userForm.user.passwordHint}" id="passwordHint" required="true" styleClass="text large">
            <v:commonsValidator type="required" arg="#{text['user.passwordHint']}"/>
        </h:inputText>
    </li>
    <li>
        <div>
            <div class="left">
                <h:outputLabel for="firstName" value="#{text['user.firstName']}" styleClass="desc"/>
                <t:message for="firstName" styleClass="fieldError"/>
                <h:inputText id="firstName" value="#{userForm.user.firstName}" maxlength="50" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.firstName']}"/>
                </h:inputText>
            </div>
            <div>
                <h:outputLabel for="lastName" value="#{text['user.lastName']}" styleClass="desc"/>
                <t:message for="lastName" styleClass="fieldError"/>
                <h:inputText value="#{userForm.user.lastName}" id="lastName" maxlength="50" required="true" styleClass="text medium">
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
                <h:inputText value="#{userForm.user.email}" id="email" size="50" required="true" styleClass="text medium">
                    <f:validator validatorId="org.apache.myfaces.validator.Email"/>
                    <v:commonsValidator type="required" arg="#{text['user.email']}"/>
                    <v:commonsValidator type="email" arg="#{text['user.email']}"/>
                </h:inputText>
            </div>
            <div>
                <h:outputLabel for="phoneNumber" value="#{text['user.phoneNumber']}" styleClass="desc"/>
                <t:message for="phoneNumber" styleClass="fieldError"/>
                <h:inputText value="#{userForm.user.phoneNumber}" id="phoneNumber" styleClass="text medium">
                    <t:validateRegExpr pattern="^\(?(\d{3})\)?[-| ]?(\d{3})[-| ]?(\d{4})$"/>
                </h:inputText>
            </div>
        </div>
    </li>
    <li>
        <h:outputLabel for="website" value="#{text['user.website']}" styleClass="desc"/>
        <t:message for="website" styleClass="fieldError"/>
        <h:inputText value="#{userForm.user.website}" id="website" size="50" required="true" styleClass="text large">
            <v:commonsValidator type="required" arg="#{text['user.website']}"/>
        </h:inputText>
    </li>
    <li>
        <label class="desc"><fmt:message key="user.address.address"/></label>
        <div class="group">
            <div>
                <h:inputText value="#{userForm.user.address.address}" id="address" size="50" styleClass="text large"/>
                <t:message for="address" styleClass="fieldError"/>
                <p><h:outputLabel for="address" value="#{text['user.address.address']}"/></p>
            </div>
            <div class="left">
                <h:inputText value="#{userForm.user.address.city}" id="city" size="40" required="true" styleClass="text medium">
                    <v:commonsValidator type="required" arg="#{text['user.address.city']}"/>
                </h:inputText>
                <t:message for="city" styleClass="fieldError"/>
                <p><h:outputLabel for="city" value="#{text['user.address.city']}"/></p>
            </div>
            <div>
                <h:inputText value="#{userForm.user.address.province}" id="province" size="2" required="true" styleClass="text state">
                    <v:commonsValidator type="required" arg="#{text['user.address.province']}"/>
                </h:inputText>
                <t:message for="province" styleClass="fieldError"/>
                <p><h:outputLabel for="province" value="#{text['user.address.province']}"/></p>
            </div>
            <div class="left">
                <h:inputText value="#{userForm.user.address.postalCode}" id="postalCode" size="10" required="true" styleClass="text zip">
                    <v:commonsValidator type="required" arg="#{text['user.address.postalCode']}"/>
                    <t:validateRegExpr pattern="^\d{5}\d*$"/>
                </h:inputText>
                <t:message for="postalCode" styleClass="fieldError"/>
                <p><h:outputLabel for="postalCode" value="#{text['user.address.postalCode']}"/></p>
            </div>
            <div>
                <h:selectOneMenu value="#{userForm.country}" id="country" required="true" styleClass="select">
                    <f:selectItems value="#{userForm.countries}"/>
                    <v:commonsValidator type="required" arg="#{text['user.address.country']}"/>
                </h:selectOneMenu>
                <t:message for="country" styleClass="fieldError"/>
                <p><h:outputLabel for="country" value="#{text['user.address.country']}"/></p>
            </div>
        </div>
    </li>
<c:choose>
    <c:when test="${param.from == 'list' or param['editUser:add'] == addText}">
    <li>
        <fieldset>
            <legend>
                <fmt:message key="userProfile.accountSettings"/>
            </legend>
        </fieldset>
    </li>
        <h:selectBooleanCheckbox value="#{userForm.user.enabled}" id="enabled" styleClass="checkbox"/>
        <label for="userForm:enabled" class="choice"><fmt:message key="user.enabled"/></label>

        <h:selectBooleanCheckbox value="#{userForm.user.accountExpired}" id="accountExpired" styleClass="checkbox"/>
        <label for="userForm:accountExpired" class="choice"><fmt:message key="user.accountExpired"/></label>

        <h:selectBooleanCheckbox value="#{userForm.user.accountLocked}" id="accountLocked" styleClass="checkbox"/>
        <label for="userForm:accountLocked" class="choice"><fmt:message key="user.accountLocked"/></label>

        <h:selectBooleanCheckbox value="#{userForm.user.credentialsExpired}" id="credentialsExpired" styleClass="checkbox"/>
        <label for="userForm:credentialsExpired" class="choice"><fmt:message key="user.credentialsExpired"/></label>
    </li>
    <li>
        <fieldset>
            <legend>
                <fmt:message key="userProfile.assignRoles"/>
            </legend>
            <h:selectManyCheckbox value="#{userForm.userRoles}" id="userRoles">
                <f:selectItems value="#{userForm.availableRoles}"/>
            </h:selectManyCheckbox>
        </fieldset>
    </li>
    </c:when>
    <c:otherwise>
    <li>
        <strong><fmt:message key="user.roles"/>:</strong>

        <c:forEach var="role" items="${userForm.userRoles}" varStatus="status">
            <c:out value="${role}"/><c:if test="${!status.last}">,</c:if>
            <input type="hidden" name="userForm:userRoles" value="<c:out value="${role}"/>" />
        </c:forEach>
        <h:inputHidden value="#{userForm.user.enabled}"/>
        <h:inputHidden value="#{userForm.user.accountExpired}"/>
        <h:inputHidden value="#{userForm.user.accountLocked}"/>
        <h:inputHidden value="#{userForm.user.credentialsExpired}"/>
    </li>
    </c:otherwise>
</c:choose>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</h:form>

<v:validatorScript functionName="validateUserForm"/>
<script type="text/javascript">
    Form.focusFirstElement($('userForm'));
    highlightFormElements();
</script>

</f:view>
