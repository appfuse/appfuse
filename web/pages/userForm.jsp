<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script> 
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
</head>

<html:form action="saveUser" focus="password" styleId="userFormEx" 
    onsubmit="return onFormSubmit(this)">
<html:hidden property="id"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>" />

<c:if test="${cookieLogin == 'true'}">
    <html:hidden property="password"/>
    <html:hidden property="confirmPassword"/>
</c:if>

<c:if test="${empty userFormEx.id}">
    <input type="hidden" name="encryptPass" value="true" />
</c:if>

<table class="detail">
<c:set var="pageButtons">
    <tr>
    	<td></td>
    	<td class="buttonBar">
            <html:submit styleClass="button" property="method" onclick="bCancel=false">
            	  <fmt:message key="button.save"/>
            </html:submit>
            
            <c:if test="${param.from == 'list'}">
            <html:submit styleClass="button" property="method"
                onclick="bCancel=false;return confirmDelete('user')" tabindex="14">
                <bean:message key="button.delete"/>
            </html:submit>
            </c:if>
        
            <html:cancel styleClass="button" property="method" onclick="bCancel=true">
                <fmt:message key="button.cancel"/>
            </html:cancel>
        </td>
    </tr>
</c:set>
    <tr>
        <th>
            <appfuse:label key="userFormEx.username"/>
        </th>
        <td>
        <c:choose>
            <c:when test="${empty userFormEx.id}">
                <html:text property="username" styleId="username" />
                <html:errors property="username"/>
            </c:when>
            <c:otherwise>
                <c:out value="${userFormEx.username}"/>
                <html:hidden property="username" 
                    styleId="username"/>
            </c:otherwise>
        </c:choose>
        </td>
    </tr>
    <c:if test="${cookieLogin != 'true'}">
    <tr>
        <th>
            <appfuse:label key="userFormEx.password"/>
        </th>
        <td>
            <html:password property="password" size="40"
                onchange="passwordChanged(this)"
                styleId="password" redisplay="true"/>
            <html:errors property="password"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.confirmPassword"/>
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
            <appfuse:label key="userFormEx.firstName"/>
        </th>
        <td>
            <html:text property="firstName" styleId="firstName"/>
            <html:errors property="firstName"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.lastName"/>
        </th>
        <td>
            <html:text property="lastName" styleId="lastName"/>
            <html:errors property="lastName"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.addressForm.address"/>
        </th>
        <td>
            <html:text property="addressForm.address" 
                styleId="addressForm.address" size="50"/>
            <html:errors property="addressForm.address"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.addressForm.city"/>
        </th>
        <td>
            <html:text property="addressForm.city" 
                styleId="addressForm.city" size="40"/>
            <html:errors property="addressForm.city"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.addressForm.province"/>
        </th>
        <td>
            <html:text property="addressForm.province" 
                styleId="addressForm.province" size="40"/>
            <html:errors property="addressForm.province"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.addressForm.country"/>
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
            <appfuse:label key="userFormEx.addressForm.postalCode"/>
        </th>
        <td>
            <html:text property="addressForm.postalCode" 
                styleId="addressForm.postalCode" size="10"/>
            <html:errors property="addressForm.postalCode"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.email"/>
        </th>
        <td>
            <html:text property="email" styleId="email" size="50"/>
            <html:errors property="email"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.phoneNumber"/>
        </th>
        <td>
            <html:text property="phoneNumber" styleId="phoneNumber"/>
            <html:errors property="phoneNumber"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.website"/>
        </th>
        <td>
            <html:text property="website" 
                styleId="website" size="50"/>
            <c:if test="${!empty userFormEx.website}">
            <a href="<c:out value="${userFormEx.website}"/>"><fmt:message key="userFormEx.visitWebsite"/></a>
            </c:if>
            <html:errors property="website"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.passwordHint"/>
        </th>
        <td>
            <html:text property="passwordHint" 
                styleId="passwordHint" size="50"/>
            <html:errors property="passwordHint"/>
        </td>
    </tr>
<c:choose>
    <c:when test="${param.from == 'list' or param.action == 'Add'}">
    <tr>
        <td></td>
        <td>
            <fieldset class="pickList">
                <legend>
                    <fmt:message key="userProfile.assignRoles"/>
                </legend>
            <table class="pickList">
                <tr>
                    <th class="pickLabel">
                        <appfuse:label key="userFormEx.availableRoles" 
                            colon="false" styleClass="required"/>
                    </th>
                    <td>
                    </td>
                    <th class="pickLabel">
                        <appfuse:label key="userFormEx.roles"
                            colon="false" styleClass="required"/>
                    </th>
                </tr>
                <c:set var="leftList" value="${availableRoles}" scope="request"/>
                <c:set var="rightList" value="${userRoles}" scope="request"/>
                <c:import url="/WEB-INF/pages/pickList.jsp">
                    <c:param name="listCount" value="1"/>
                    <c:param name="leftId" value="availableRoles"/>
                    <c:param name="rightId" value="userRoles"/>
                </c:import>
            </table>
            </fieldset>
        </td>
    </tr>
    </c:when>
    <c:when test="${not empty userFormEx.id}">
    <tr>
        <th>
            <appfuse:label key="userFormEx.roles"/>
        </th>
        <td>
        <c:forEach var="role" items="${userFormEx.roles}" varStatus="status">
            <c:out value="${role.roleName}"/><c:if test="${!status.last}">,</c:if>
            <input type="hidden" name="userRoles" 
                value="<c:out value="${role.roleName}"/>" />
        </c:forEach>
        </td>
    </tr>
    </c:when>
</c:choose>

<%-- Print out buttons - defined at top of form --%>
<c:out value="${pageButtons}" escapeXml="false" />

</table>
</html:form>

<script type="text/javascript">
<!--
highlightFormElements();
<%-- if we're doing an add, change the focus --%>
<c:if test="${param.action == 'Add'}">document.forms[0].username.focus();</c:if>

// This function is a workaround for the Validator not working 
// with LookupDispatchAction.
function cancel() {
    location.href = '<html:rewrite forward="cancelUser"/>&from=<c:out value="${param.from}"/>';
}

function deleteUser() {
    location.href = '<html:rewrite forward="deleteUser"/>&username=<c:out value="${userFormEx.username}"/>';
}

function passwordChanged(passwordField) {
    var origPassword = "<c:out value="${userFormEx.password}"/>";
    if (passwordField.value != origPassword) {
        createFormElement("input", "hidden", 
                          "encryptPass", "encryptPass", 
                          "true", passwordField.form);
    }
}

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
<c:if test="${param.from == 'list'}">
    selectAll('userRoles');
</c:if>
    return validateUserFormEx(theForm);
}
// -->
</script>

<html:javascript formName="userFormEx" cdata="false"
      dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"
      src="<c:url value="/scripts/validator.jsp"/>"></script>


