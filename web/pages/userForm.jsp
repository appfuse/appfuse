<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
    <meta name="menu" content="UserMenu"/>
</head>

<html:form action="saveUser" styleId="userForm" onsubmit="return validateUserForm(this)">
<html:hidden property="id"/>
<html:hidden property="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>

<c:if test="${cookieLogin == 'true'}">
    <html:hidden property="password"/>
    <html:hidden property="confirmPassword"/>
</c:if>

<c:if test="${empty userForm.username}">
    <input type="hidden" name="encryptPass" value="true"/>
</c:if>

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <html:submit styleClass="button" property="method.save" onclick="bCancel=false">
                <fmt:message key="button.save"/>
            </html:submit>

            <c:if test="${param.from == 'list' and param.method != 'Add'}">
            <html:submit styleClass="button" property="method.delete" onclick="bCancel=true; return confirmDelete('User')">
                <fmt:message key="button.delete"/>
            </html:submit>
            </c:if>

            <html:cancel styleClass="button" onclick="bCancel=true">
                <fmt:message key="button.cancel"/>
            </html:cancel>
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
        <appfuse:label styleClass="desc" key="userForm.username"/>
        <html:errors property="username"/>
        <html:text styleClass="text large" property="username" styleId="username"/>
    </li>
    <c:if test="${cookieLogin != 'true'}">
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="userForm.password"/>
                <html:errors property="password"/>
                <html:password styleClass="text medium" property="password" onchange="passwordChanged(this)"
                    styleId="password" redisplay="true"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="userForm.confirmPassword"/>
                <html:errors property="confirmPassword"/>
                <html:password styleClass="text medium" property="confirmPassword" styleId="confirmPassword" redisplay="true"/>
            </div>
        </div>
    </li>
    </c:if>
    <li>
        <appfuse:label styleClass="desc" key="userForm.passwordHint"/>
        <html:errors property="passwordHint"/>
        <html:text styleClass="text large" property="passwordHint" styleId="passwordHint"/>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="userForm.firstName"/>
                <html:errors property="firstName"/>
                <html:text styleClass="text medium" property="firstName" styleId="firstName" maxlength="50"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="userForm.lastName"/>
                <html:errors property="lastName"/>
                <html:text styleClass="text medium" property="lastName" styleId="lastName" maxlength="50"/>
            </div>
        </div>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="userForm.email"/>
                <html:errors property="email"/>
                <html:text styleClass="text medium" property="email" styleId="email"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="userForm.phoneNumber"/>
                <html:errors property="phoneNumber"/>
                <html:text styleClass="text medium" property="phoneNumber" styleId="phoneNumber"/>
            </div>
        </div>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="userForm.website"/>
        <html:errors property="website"/>
        <html:text styleClass="text large" property="website" styleId="website"/>
    </li>
    <li>
        <label class="desc"><fmt:message key="userForm.addressForm.address"/></label>
        <div class="group">
            <div>
                <html:text styleClass="text large" property="addressForm.address"
                    styleId="addressForm.address"/>
                <html:errors property="addressForm.address"/>
                <p><appfuse:label key="userForm.addressForm.address"/></p>
            </div>
            <div class="left">
                <html:text styleClass="text medium" property="addressForm.city"
                    styleId="addressForm.city"/>
                <html:errors property="addressForm.city"/>
                <p><appfuse:label key="userForm.addressForm.city"/></p>
            </div>
            <div>
                <html:text styleClass="text state" property="addressForm.province"
                    styleId="addressForm.province" size="2"/>
                <html:errors property="addressForm.province"/>
                <p><appfuse:label key="userForm.addressForm.province"/></p>
            </div>
            <div class="left">
                <html:text styleClass="text zip" property="addressForm.postalCode"
                    styleId="addressForm.postalCode"/>
                <html:errors property="addressForm.postalCode"/>
                <p><appfuse:label key="userForm.addressForm.postalCode"/></p>
            </div>
            <div>
                <appfuse:country name="countries" toScope="page"/>
                <html:select property="addressForm.country" styleClass="select">
                    <html:option value=""/>
                    <html:options collection="countries"
                        property="value" labelProperty="label"/>
                </html:select>
                <html:errors property="addressForm.country"/>
                <p><appfuse:label key="userForm.addressForm.country"/></p>
            </div>
        </div>
    </li>
<c:choose>
    <c:when test="${param.from == 'list' or param.method == 'Add'}">
    <li>
        <fieldset>
            <legend><fmt:message key="userProfile.accountSettings"/></legend>
            <html:checkbox styleClass="checkbox" property="enabled" styleId="enabled"/>
            <label for="enabled" class="choice"><fmt:message key="userForm.enabled"/></label>

            <html:checkbox styleClass="checkbox" property="accountExpired" styleId="accountExpired"/>
            <label for="accountExpired" class="choice"><fmt:message key="userForm.accountExpired"/></label>

            <html:checkbox styleClass="checkbox" property="accountLocked" styleId="accountLocked"/>
            <label for="accountLocked" class="choice"><fmt:message key="userForm.accountLocked"/></label>

            <html:checkbox styleClass="checkbox" property="credentialsExpired" styleId="credentialsExpired"/>
            <label for="credentialsExpired" class="choice"><fmt:message key="userForm.credentialsExpired"/></label>
        </fieldset>
    </li>
    <li>
        <fieldset>
            <legend><fmt:message key="userProfile.assignRoles"/></legend>
            <c:forEach var="role" items="${availableRoles}">
                <html-el:multibox styleClass="checkbox" property="userRoles" styleId="${role.label}">
                    <c:out value="${role.value}"/>
                </html-el:multibox>
                <label class="choice" for="<c:out value="${role.label}"/>">
                    <c:out value="${role.label}"/>
                </label>
            </c:forEach>
        </fieldset>
    </li>
    </c:when>
    <c:when test="${not empty userForm.username}">
    <li>
        <strong><fmt:message key="userForm.roles"/>:</strong>

        <c:forEach var="role" items="${userForm.roles}" varStatus="status">
            <c:out value="${role.name}"/><c:if test="${!status.last}">,</c:if>
            <input type="hidden" name="userRoles" value="<c:out value="${role.name}"/>"/>
        </c:forEach>
        <html:hidden property="enabled"/>
        <html:hidden property="accountExpired"/>
        <html:hidden property="accountLocked"/>
        <html:hidden property="credentialsExpired"/>
    </li>
    </c:when>
</c:choose>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</html:form>

<script type="text/javascript">
    Form.focusFirstElement($('userForm'));
    highlightFormElements();

    function passwordChanged(passwordField) {
        var origPassword = "<c:out value="${userForm.password}"/>";
        if (passwordField.value != origPassword) {
            createFormElement("input", "hidden",
                              "encryptPass", "encryptPass",
                              "true", passwordField.form);
        }
    }
</script>

<html:javascript formName="userForm" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

