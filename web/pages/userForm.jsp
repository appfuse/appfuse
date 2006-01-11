<%@ include file="/common/taglibs.jsp"%>

<html:form action="saveUser" focus="password" styleId="userForm"
    onsubmit="return validateUserForm(this)">
<html:hidden property="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>

<c:if test="${cookieLogin == 'true'}">
    <html:hidden property="password"/>
    <html:hidden property="confirmPassword"/>
</c:if>

<c:if test="${empty userForm.username}">
    <input type="hidden" name="encryptPass" value="true"/>
</c:if>

<table class="detail">
<c:set var="pageButtons">
    <tr>
        <td></td>
        <td class="buttonBar">
            <html:submit styleClass="button" property="method.save" onclick="bCancel=false">
                  <fmt:message key="button.save"/>
            </html:submit>

            <c:if test="${param.from == 'list'}">
            <html:submit styleClass="button" property="method.delete"
                onclick="bCancel=true; return confirmDelete('User')">
                <fmt:message key="button.delete"/>
            </html:submit>
            </c:if>

            <html:cancel styleClass="button" onclick="bCancel=true">
                <fmt:message key="button.cancel"/>
            </html:cancel>
        </td>
    </tr>
</c:set>
    <tr>
        <th>
            <appfuse:label key="userForm.username"/>
        </th>
        <td>
        <c:choose>
            <c:when test="${empty userForm.username}">
                <html:text property="username" styleId="username"/>
                <html:errors property="username"/>
            </c:when>
            <c:otherwise>
                <c:out value="${userForm.username}"/>
                <html:hidden property="username" styleId="username"/>
            </c:otherwise>
        </c:choose>
        </td>
    </tr>
    <c:if test="${cookieLogin != 'true'}">
    <tr>
        <th>
            <appfuse:label key="userForm.password"/>
        </th>
        <td>
            <html:password property="password" size="40" onchange="passwordChanged(this)"
                styleId="password" redisplay="true"/>
            <html:errors property="password"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.confirmPassword"/>
        </th>
        <td>
            <html:password property="confirmPassword" size="40"
                styleId="confirmPassword" redisplay="true"/>
            <html:errors property="confirmPassword"/>
        </td>
    </tr>
    </c:if>
    <tr>
        <th>
            <appfuse:label key="userForm.firstName"/>
        </th>
        <td>
            <html:text property="firstName" styleId="firstName" maxlength="50"/>
            <html:errors property="firstName"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.lastName"/>
        </th>
        <td>
            <html:text property="lastName" styleId="lastName" maxlength="50"/>
            <html:errors property="lastName"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.addressForm.address"/>
        </th>
        <td>
            <html:text property="addressForm.address"
                styleId="addressForm.address" size="50"/>
            <html:errors property="addressForm.address"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.addressForm.city"/>
        </th>
        <td>
            <html:text property="addressForm.city"
                styleId="addressForm.city" size="40"/>
            <html:errors property="addressForm.city"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.addressForm.province"/>
        </th>
        <td>
            <html:text property="addressForm.province"
                styleId="addressForm.province" size="40"/>
            <html:errors property="addressForm.province"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.addressForm.country"/>
        </th>
        <td>
            <appfuse:country name="countries" toScope="page"/>
            <html:select property="addressForm.country">
                <html:option value=""/>
                <html:options collection="countries"
                    property="value" labelProperty="label"/>
            </html:select>
            <html:errors property="addressForm.country"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.addressForm.postalCode"/>
        </th>
        <td>
            <html:text property="addressForm.postalCode"
                styleId="addressForm.postalCode" size="10"/>
            <html:errors property="addressForm.postalCode"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.email"/>
        </th>
        <td>
            <html:text property="email" styleId="email" size="50"/>
            <html:errors property="email"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.phoneNumber"/>
        </th>
        <td>
            <html:text property="phoneNumber" styleId="phoneNumber"/>
            <html:errors property="phoneNumber"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.website"/>
        </th>
        <td>
            <html:text property="website" styleId="website" size="50"/>
            <c:if test="${!empty userForm.website}">
            <a href="<c:out value="${user.website}"/>"><fmt:message key="userForm.visitWebsite"/></a>
            </c:if>
            <html:errors property="website"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.passwordHint"/>
        </th>
        <td>
            <html:text property="passwordHint" styleId="passwordHint" size="50"/>
            <html:errors property="passwordHint"/>
        </td>
    </tr>
<c:choose>
    <c:when test="${param.from == 'list' or param.method == 'Add'}">
    <tr>
        <td></td>
        <td>
            <fieldset class="pickList">
                <legend>
                    <fmt:message key="userProfile.accountSettings"/>
                </legend>
                <table class="pickList">
                    <tr>
                        <td>
                            <html:checkbox property="enabled" styleId="enabled"/>
                            <label for="enabled"><fmt:message key="userForm.enabled"/></label>
                        
                            <html:checkbox property="accountExpired" styleId="accountExpired"/>
                            <label for="accountExpired"><fmt:message key="userForm.accountExpired"/></label>

                            <html:checkbox property="accountLocked" styleId="accountLocked"/>
                            <label for="accountLocked"><fmt:message key="userForm.accountLocked"/></label>

                            <html:checkbox property="credentialsExpired" styleId="credentialsExpired"/>
                            <label for="credentialsExpired"><fmt:message key="userForm.credentialsExpired"/></label>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td></td>
        <td>
            <fieldset class="pickList">
                <legend>
                    <fmt:message key="userProfile.assignRoles"/>
                </legend>
                <table class="pickList">
                    <tr>
                        <td>
                        <c:forEach var="role" items="${availableRoles}">
                            <html-el:multibox property="userRoles" styleId="${role.label}">
                                <c:out value="${role.value}"/>
                            </html-el:multibox>
                            <label for="<c:out value="${role.label}"/>">
                                <c:out value="${role.label}"/>
                            </label>
                        </c:forEach>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    </c:when>
    <c:when test="${not empty userForm.username}">
    <tr>
        <th>
            <appfuse:label key="userForm.roles"/>
        </th>
        <td>
        <c:forEach var="role" items="${userForm.roles}" varStatus="status">
            <c:out value="${role.name}"/><c:if test="${!status.last}">,</c:if>
            <input type="hidden" name="userRoles" value="<c:out value="${role.name}"/>"/>
        </c:forEach>
            <html:hidden property="enabled"/>
            <html:hidden property="accountExpired"/>
            <html:hidden property="accountLocked"/>
            <html:hidden property="credentialsExpired"/>                        
        </td>
    </tr>
    </c:when>
</c:choose>

    <%-- Print out buttons - defined at top of form --%>
    <%-- This is so you can put them at the top and the bottom if you like --%>
    <c:out value="${pageButtons}" escapeXml="false"/>

</table>
</html:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms["userForm"]);
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


