<%@ include file="/common/taglibs.jsp"%>

<f:view>
<f:loadBundle var="text" basename="#{userForm.bundleName}"/>

<h:form id="userForm" onsubmit="return validateUserForm(this)">
<h:inputHidden value="#{userForm.user.version}" id="version">
    <f:convertNumber/>
</h:inputHidden>
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

<h:panelGrid columns="3" styleClass="detail" columnClasses="label">

    <h:outputLabel for="username" value="#{text['user.username']}"/>
    
    <c:choose>
    <c:when test="${empty userForm.user.username}">
        <h:inputText value="#{userForm.user.username}" id="username" required="true">
            <v:commonsValidator type="required" arg="#{text['user.username']}"/>
        </h:inputText>
        <t:message for="username" styleClass="fieldError"/>
    </c:when>
    <c:otherwise>
        <h:outputText value="#{userForm.user.username}"/>
        <h:inputHidden value="#{userForm.user.username}" id="username" required="true"/>
    </c:otherwise>
    </c:choose>
    
<c:if test="${cookieLogin != 'true'}">

    <h:outputLabel for="password" value="#{text['user.password']}"/>

    <h:inputSecret value="#{userForm.user.password}" id="password"
        size="40" redisplay="true" required="true">
        <v:commonsValidator type="required" arg="#{text['user.password']}"/>
    </h:inputSecret>
    <t:message for="password" styleClass="fieldError"/>

    <h:outputLabel for="confirmPassword" value="#{text['user.confirmPassword']}"/>

    <h:inputSecret value="#{userForm.user.confirmPassword}" id="confirmPassword"
        size="40" redisplay="true" required="true">
        <v:commonsValidator type="required" arg="#{text['user.confirmPassword']}"/>
        <t:validateEqual for="password"/>
    </h:inputSecret>
    <t:message for="confirmPassword" styleClass="fieldError"/>

</c:if>
    
    <h:outputLabel for="firstName" value="#{text['user.firstName']}"/>

    <h:inputText id="firstName" value="#{userForm.user.firstName}" maxlength="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.firstName']}"/>
    </h:inputText>
    <t:message for="firstName" styleClass="fieldError"/>

    <h:outputLabel for="lastName" value="#{text['user.lastName']}"/>

    <h:inputText value="#{userForm.user.lastName}" id="lastName" maxlength="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.lastName']}"/>
    </h:inputText>
    <t:message for="lastName" styleClass="fieldError"/>

    <h:outputLabel for="address" value="#{text['user.address.address']}"/>

    <h:inputText value="#{userForm.user.address.address}" id="address" size="50"/>
    <t:message for="address" styleClass="fieldError"/>

    <h:outputLabel for="city" value="#{text['user.address.city']}"/>

    <h:inputText value="#{userForm.user.address.city}" id="city" size="40" required="true">
        <v:commonsValidator type="required" arg="#{text['user.address.city']}"/>
    </h:inputText>
    <t:message for="city" styleClass="fieldError"/>

    <h:outputLabel for="province" value="#{text['user.address.province']}"/>

    <h:inputText value="#{userForm.user.address.province}" id="province" size="40" required="true">
        <v:commonsValidator type="required" arg="#{text['user.address.province']}"/>
    </h:inputText>
    <t:message for="province" styleClass="fieldError"/>

    <h:outputLabel for="country" value="#{text['user.address.country']}"/>

    <h:selectOneMenu value="#{userForm.country}" id="country" required="true">
        <f:selectItems value="#{userForm.countries}"/>
        <v:commonsValidator type="required" arg="#{text['user.address.country']}"/>
    </h:selectOneMenu>
    <t:message for="country" styleClass="fieldError"/>

    <h:outputLabel for="postalCode" value="#{text['user.address.postalCode']}"/>

    <h:inputText value="#{userForm.user.address.postalCode}" id="postalCode" size="10" required="true">
        <v:commonsValidator type="required" arg="#{text['user.address.postalCode']}"/>
        <t:validateRegExpr pattern="^\d{5}\d*$"/>
    </h:inputText>
    <t:message for="postalCode" styleClass="fieldError"/>

    <h:outputLabel for="email" value="#{text['user.email']}"/>

    <h:inputText value="#{userForm.user.email}" id="email" size="50" required="true">
        <f:validator validatorId="org.apache.myfaces.validator.Email"/>>
        <v:commonsValidator type="required" arg="#{text['user.email']}"/>
        <v:commonsValidator type="email" arg="#{text['user.email']}"/>
    </h:inputText>
    <t:message for="email" styleClass="fieldError"/>

    <h:outputLabel for="phoneNumber" value="#{text['user.phoneNumber']}"/>

    <h:inputText value="#{userForm.user.phoneNumber}" id="phoneNumber">
        <t:validateRegExpr pattern="^\(?(\d{3})\)?[-| ]?(\d{3})[-| ]?(\d{4})$"/>
    </h:inputText>
    <t:message for="phoneNumber" styleClass="fieldError"/>

    <h:outputLabel for="website" value="#{text['user.website']}"/>

    <h:inputText value="#{userForm.user.website}" id="website" size="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.website']}"/>
    </h:inputText>
    <t:message for="website" styleClass="fieldError"/>

    <h:outputLabel for="passwordHint" value="#{text['user.passwordHint']}"/>

    <h:inputText value="#{userForm.user.passwordHint}" id="passwordHint" size="50" required="true">
        <v:commonsValidator type="required" arg="#{text['user.passwordHint']}"/>
    </h:inputText>
    <t:message for="passwordHint" styleClass="fieldError"/>

<c:choose>
    <c:when test="${param.from == 'list' or param['editUser:add'] == 'Add'}">

        <f:verbatim><label for="userForm:enabled"><fmt:message key="user.enabled"/>?</label></f:verbatim>        
        <h:selectBooleanCheckbox value="#{userForm.user.enabled}" id="enabled" style="margin-left: 7px"/>
        <t:message for="enabled" styleClass="fieldError"/>
  
        <h:outputLabel for="userRoles" value="#{text['userProfile.assignRoles']}"/>
    
        <h:selectManyCheckbox value="#{userForm.userRoles}" id="userRoles">
            <f:selectItems value="#{userForm.availableRoles}"/>
        </h:selectManyCheckbox>
        <t:message for="userRoles" styleClass="fieldError"/>
    </c:when>
    <c:otherwise>

        <h:outputLabel for="userRoles" value="#{text['user.roles']}"/>
 
        <f:verbatim>
            <c:forEach var="role" items="${userForm.userRoles}" varStatus="status">
                <c:out value="${role}"/><c:if test="${!status.last}">,</c:if>
                <input type="hidden" name="userForm:userRoles" value="<c:out value="${role}"/>" />
            </c:forEach>
        </f:verbatim>
        <h:inputHidden value="#{userForm.user.enabled}" id="enabled"/>
    </c:otherwise>
</c:choose>
    <%-- Put in empty <td></td> --%>
    <h:inputHidden value=""/>
    
    <h:panelGroup styleClass="buttonBar">
    	<h:commandButton value="#{text['button.save']}" action="#{userForm.save}" 
            id="save" styleClass="button"/>
    
        <c:if test="${param.from == 'list'}">
        <h:commandButton value="#{text['button.delete']}" action="#{userForm.delete}" 
            id="delete" styleClass="button" onclick="bCancel=false"/>
        </c:if> <%-- Removed confirm since WebTest can't handle it: ;return confirmDelete('User') --%>
    
        <h:commandButton value="#{text['button.cancel']}" action="#{userForm.cancel}" immediate="true"  
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
    
    <h:inputHidden value=""/>
</h:panelGrid>
</h:form>

<v:validatorScript functionName="validateUserForm"/>
<script type="text/javascript">
<c:if test="${empty userForm.user.username}">
    document.forms["userForm"].elements["userForm:username"].focus();
</c:if>
<c:if test="${not empty userForm.user.username}">
    document.forms["userForm"].elements["userForm:password"].focus();
</c:if>
    highlightFormElements();
</script>

</f:view>
