<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="signup.title"/></title>
<content tag="heading"><fmt:message key="signup.heading"/></content>
<body id="signup"/>

<fmt:message key="signup.message"/>

<div class="separator"></div>

<html:form action="/signup" focus="username" styleId="userForm"
    onsubmit="return validateUserForm(this)">

<table class="detail">
    <tr>
        <th>
            <appfuse:label key="userForm.username"/>
        </th>
        <td>
            <html:text property="username" styleId="username"/>
            <html:errors property="username"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.password"/>
        </th>
        <td>
            <html:password property="password" size="40"
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
    <tr>
        <th>
            <appfuse:label key="userForm.firstName"/>
        </th>
        <td>
            <html:text property="firstName" styleId="firstName"/>
            <html:errors property="firstName"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.lastName"/>
        </th>
        <td>
            <html:text property="lastName" styleId="lastName"/>
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
            <html:errors property="addressForm.countries"/>
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
            <a href="<c:out value="${userForm.website}"/>"><fmt:message key="userForm.visitWebsite"/></a>
            </c:if>
            <html:errors property="website"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userForm.passwordHint"/>
        </th>
        <td>
            <html:text property="passwordHint"
                styleId="passwordHint" size="50"/>
            <html:errors property="passwordHint"/>
        </td>
    </tr>
    <tr>
    	<td></td>
    	<td class="buttonBar">
            <html:submit styleClass="button" onclick="bCancel=false">
            	  <fmt:message key="button.register"/>
            </html:submit>

            <html:cancel styleClass="button" onclick="bCancel=true">
                <fmt:message key="button.cancel"/>
            </html:cancel>
        </td>
    </tr>
</table>
</html:form>

<html:javascript formName="userForm" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>