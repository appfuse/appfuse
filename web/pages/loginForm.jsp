<%@ include file="/common/taglibs.jsp"%>

<div id="loginTable">
<%-- If you don't want to encrypt passwords programmatically, or you don't
     care about using SSL for the login, you can change this form's action
     to "j_security_check" --%>
<form method="post" id="loginForm" action="<c:url value="/authorize"/>" 
    onsubmit="saveUsername(this);return validateForm(this)">
<table width="100%">
    <tr>
        <td colspan="2">
            <c:if test="${param.error != null}">
            <div class="error" 
                style="margin-right: 0; margin-bottom: 3px; margin-top: 3px">
                    <html:img pageKey="icon.warning.img" 
                        altKey="icon.warning" styleClass="icon"/>
                    <fmt:message key="errors.password.mismatch"/>
                </div>
            </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <label for="j_username" class="required">
                * <fmt:message key="label.username"/>:
            </label>
        </th>
        <td>
            <input type="text" name="j_username" id="j_username" size="25" tabindex="1" />
        </td>
    </tr>
    <tr>
        <th>
            <label for="j_password" class="required">
                * <fmt:message key="label.password"/>:
            </label>
        </th>
        <td>
            <input type="password" name="j_password" id="j_password" size="20" tabindex="2" />
        </td>
    </tr>
    <c:if test="${rememberMeEnabled}">
    <tr>
        <td></td>
        <td>
            <input type="checkbox" name="rememberMe" id="rememberMe" tabindex="3" />
            <label for="rememberMe"><fmt:message key="login.rememberMe"/></label>
        </td>
    </tr>
    </c:if>
    <tr>
        <td></td>
        <td>
        	<!-- for Resin -->
        	<input type="hidden" name="j_uri" id="j_uri" value="" />
            <input type="submit" class="button" name="login" id="login" value="<fmt:message key="button.login"/>" tabindex="4" />
        	<input type="reset" class="button" name="reset" id="reset" value="<fmt:message key="button.reset"/>" tabindex="5" 
                onclick="document.getElementById('j_username').focus()" />
        </td>
    </tr>
    <tr>
		<td></td>
		<td><br />
            <%-- Doesn't work on Resin
            <fmt:message key="login.signup">
                <fmt:param><c:url value="/signup.jsp"/></fmt:param>
            </fmt:message>
            --%>
            <bean-el:message key="login.signup" 
                arg0="${pageContext.request.contextPath}/signup.jsp"/>
        </td>
	</tr>
</table>
</form>
</div>

<%@ include file="/scripts/login.js"%>
