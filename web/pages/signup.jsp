<%@ include file="/common/taglibs.jsp"%>

<fmt:message key="signup.message"/>

<div class="separator"></div>

<form action="<c:url value="/register"/>" name="userFormEx" method="post"
	id="userFormEx" onsubmit="return validateUserFormEx(this)">

<table class="detail">
    <tr>
        <th>
            <label class="required" for="username">
                <fmt:message key="userFormEx.username"/>*:
            </label>
        </th>
        <td>
            <input type="text" name="username" id="username"
                value="<c:out value="${param.username}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="password">
                <fmt:message key="userFormEx.password"/>*:
            </label>
        </th>
        <td>
            <input type="password" name="password" id="password"
                value="<c:out value="${param.password}"/>"/>
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="confirmPassword">
                <fmt:message key="userFormEx.confirmPassword"/>*:
            </label>
        </th>
        <td>
            <input type="password" name="confirmPassword" id="confirmPassword"
                value="<c:out value="${param.confirmPassword}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="firstName">
                <fmt:message key="userFormEx.firstName"/>*:
            </label>
        </th>
        <td>
            <input type="text" name="firstName" id="firstName"
                value="<c:out value="${param.firstName}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="lastName">
                <fmt:message key="userFormEx.lastName"/>*:
            </label>
        </th>
        <td>
            <input type="text" name="lastName" id="lastName"
                value="<c:out value="${param.lastName}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label for="address">
                <fmt:message key="userFormEx.address"/>:
            </label>
        </th>
        <td>
            <input type="text" name="address" size="50" id="address"
                value="<c:out value="${param.address}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="city">
                <fmt:message key="userFormEx.city"/>*:
            </label>
        </th>
        <td>
            <input type="text" name="city" size="40" id="city"
                value="<c:out value="${param.city}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required">
                <fmt:message key="userFormEx.province"/>*:
            </label>
        </th>
        <td>
        <c:if test="${userFormEx.province != null}">
            <bean:define id="state" name="userFormEx" 
                property="province" type="java.lang.String" />
            <state:state name="province" default="<%=state%>"/>
        </c:if>
        <c:if test="${userFormEx.province == null}">
            <state:state name="province" prompt=""/>
        </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <label class="required">
                <fmt:message key="userFormEx.country"/>*:
            </label>
        </th>
        <td>
        <c:if test="${userFormEx.country != null}">
            <bean:define id="country" name="userFormEx" 
                property="country" type="java.lang.String" />
            <country:country name="country" default="<%=country%>"/>
        </c:if>
        <c:if test="${userFormEx.country == null}">
            <country:country name="country" prompt=""/>
        </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="postalCode">
                <fmt:message key="userFormEx.postalCode"/>*:
            </label>
        </th>
        <td>
			<input type="text" name="postalCode" id="postalCode"
                value="<c:out value="${param.postalCode}"/>" size="10" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="email">
                <fmt:message key="userFormEx.email"/>*:
            </label>
        </th>
        <td>
            <input type="text" name="email" size="50" id="email"
                value="<c:out value="${param.email}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label for="phoneNumber">
                <fmt:message key="userFormEx.phoneNumber"/>:
            </label>
        </th>
        <td>
            <input type="text" name="phoneNumber" id="phoneNumber"
                value="<c:out value="${param.phoneNumber}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="website">
                <fmt:message key="userFormEx.website"/>*:
            </label>
        </th>
        <td>
            <input type="text" name="website" size="50" id="website"
                value="<c:out value="${param.website}"/>" />
        </td>
    </tr>
    <tr>
        <th>
            <label class="required" for="passwordHint">
                <fmt:message key="userFormEx.passwordHint"/>*:
            </label>
        </th>
        <td>
            <input type="text" name="passwordHint" size="50" id="passwordHint"
                value="<c:out value="${param.passwordHint}"/>" />
        </td>
    </tr>
    <tr>
    	<td></td>
    	<td class="buttonBar">
            <html:submit styleClass="button" property="action" 
                onclick="bCancel=false">
            	<fmt:message key="button.register"/>
            </html:submit>

            <input type="button" value="<fmt:message key="button.cancel"/>"
                class="button" onclick="location.href='<c:url value="/"/>'" />
        </td>
    </tr>

</table>
</form>
<script type="text/javascript">
<!--
  var focusControl = document.forms["userFormEx"].elements["username"];

  if (focusControl.type != "hidden") {
     focusControl.focus();
  }
// -->
</script>
<html:javascript formName="userFormEx" cdata="false"
      dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"
      src="<c:url value="/scripts/validator.jsp"/>"></script>