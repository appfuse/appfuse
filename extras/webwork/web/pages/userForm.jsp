<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script> 
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
</head>

<ww:form name="'userForm'" action="'saveUser'" method="'post'" validate="true">
<ww:hidden name="'user.id'" value="user.id"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>" />

<c:if test="${cookieLogin == 'true'}">
	<ww:hidden name="'user.password'" value="user.password"/>
	<ww:hidden name="'user.confirmPassword'" value="user.confirmPassword"/>
</c:if>

<ww:if test="user.id == null">
    <input type="hidden" name="encryptPass" value="true" />
</ww:if>

<c:set var="pageButtons">
    <tr>
    	<td></td>
    	<td class="buttonBar">
            <input type="submit" class="button" name="save" 
                value="<fmt:message key="button.save"/>" 
                onclick="onFormSubmit(this.form)"/>
            
        <c:if test="${param.from == 'list'}">
            <input type="submit" class="button" name="delete"
                onclick="return confirmDelete('user')" 
                value="<fmt:message key="button.delete"/>" />
        </c:if>
        
            <input type="submit" class="button" name="cancel"
                value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</c:set>

	<ww:if test="user.id == null">
	    <ww:textfield label="getText('user.username')" name="'user.username'" 
	        value="user.username" required="true"/>
	</ww:if>
	<ww:else>
    <tr>
        <th>
            <label class="required">* <fmt:message key="user.username"/>:</label>
        </th>
        <td>
    		<ww:property value="user.username"/>
    		<ww:hidden name="'user.username'" value="user.username"/>
        </td>
    </tr>
	</ww:else>
	
	<c:if test="${cookieLogin != 'true'}">
	    <ww:password label="getText('user.password')" name="'user.password'" show="true"
	        value="user.password" required="true" size="40" onchange="passwordChanged(this)"/>
	    <ww:password label="getText('user.confirmPassword')" name="'user.confirmPassword'" 
	        value="user.confirmPassword" required="true" show="true" size="40"/>
 	</c:if>

    <ww:textfield label="getText('user.firstName')" name="'user.firstName'"
        value="user.firstName" required="true"/>
    <ww:textfield label="getText('user.lastName')" name="'user.lastName'"
        value="user.lastName" required="true"/>
    <ww:textfield label="getText('user.address.address')" name="'user.address.address'"
        value="user.address.address" size="50"/>
    <ww:textfield label="getText('user.address.city')" name="'user.address.city'"
        value="user.address.city" size="40" required="true"/>
    <ww:textfield label="getText('user.address.province')" name="'user.address.province'"
        value="user.address.province" required="true"/>
    <tr>
        <th>
            <label for="user.address.country" class="required">
            	* <fmt:message key="user.address.country"/>:
            </label>
        </th>
        <td>
       	    <ww:set name="country" value="user.address.country" scope="page"/>
            <appfuse:country name="user.address.country" prompt="" default="${country}"/>
        </td>
    </tr>
    <ww:textfield label="getText('user.address.postalCode')" name="'user.address.postalCode'"
        value="user.address.postalCode" required="true" size="10"/>
    <ww:textfield label="getText('user.email')" name="'user.email'"
        value="user.email" required="true" size="50"/>
    <ww:textfield label="getText('user.phoneNumber')" name="'user.phoneNumber'"
        value="user.phoneNumber"/>
    <ww:textfield label="getText('user.website')" name="'user.website'"
        value="user.website" required="true" size="50"/>
    <ww:textfield label="getText('user.passwordHint')" name="'user.passwordHint'"
        value="user.passwordHint" required="true" size="50"/>
        
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
                        <label class="required">
                            <fmt:message key="user.availableRoles"/>
                        </label>
                    </th>
                    <td>
                    </td>
                    <th class="pickLabel">
                        <label class="required">
                            <fmt:message key="user.roles"/>
                        </label>
                    </th>
                </tr>
                <c:set var="leftList" value="${availableRoles}" scope="request"/>
                <ww:set name="rightList" value="user.roleList" scope="request"/>
                <c:import url="/WEB-INF/pages/pickList.jsp">
                    <c:param name="listCount" value="1"/>
                    <c:param name="leftId" value="availableRoles"/>
                    <c:param name="rightId" value="user.userRoles"/>
                </c:import>
            </table>
            </fieldset>
        </td>
    </tr>
    </c:when>
    <c:otherwise>
    <tr>
        <th>
            <label><fmt:message key="user.roles"/>:</label>
        </th>
        <td>
            <ww:iterator value="user.roleList" status="status">
              	<ww:property value="label"/><ww:if test="!#status.last">,</ww:if> 
              	<input type="hidden" name="user.userRoles"
              		value="<ww:property value="value"/>" />
            </ww:iterator>
        </td>
    </tr>
    </c:otherwise>
</c:choose>

<%-- Print out buttons - defined at top of form --%>
<c:out value="${pageButtons}" escapeXml="false" />

</ww:form>

<script type="text/javascript">
<!--
highlightFormElements();

<c:if test="${param.method == 'Add'}">
var focusControl = document.forms["userForm"].elements["user.username"];
</c:if>
<c:if test="${param.method != 'Add'}">
var focusControl = document.forms["userForm"].elements["user.password"];
</c:if>

if (focusControl.type != "hidden" && !focusControl.disabled) {
    focusControl.focus();
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
    selectAll('user.userRoles');
</c:if>
    //return validateUserFormEx(theForm);
}
// -->
</script>


