<%@ include file="/common/taglibs.jsp"%>

<fmt:message key="signup.message"/>

<div class="separator"></div>

<html:form action="/signup" focus="username" styleId="userFormEx"
    onsubmit="return validateUserFormEx(this)">

<table class="detail">
    <tr>
        <th>
            <appfuse:label key="userFormEx.username"/>
        </th>
        <td>
            <html:text property="username" styleId="username" />
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.password"/>
        </th>
        <td>
            <html:password property="password" size="40"
                styleId="password" redisplay="true"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.confirmPassword"/>
        </th>
        <td>
            <html:password property="confirmPassword" size="40"
                styleId="confirmPassword" redisplay="true"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.firstName"/>
        </th>
        <td>
            <html:text property="firstName"
                styleId="firstName"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.lastName"/>
        </th>
        <td>
            <html:text property="lastName"
                styleId="lastName"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.address"/>
        </th>
        <td>
            <html:text property="address"
                styleId="address" size="50"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.city"/>
        </th>
        <td>
            <html:text property="city"
                styleId="city" size="40"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.province"/>
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
            <appfuse:label key="userFormEx.country"/>
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
            <appfuse:label key="userFormEx.postalCode"/>
        </th>
        <td>
            <html:text property="postalCode"
                styleId="postalCode" size="10"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.email"/>
        </th>
        <td>
            <html:text property="email"
                styleId="email" size="50"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.phoneNumber"/>
        </th>
        <td>
            <html:text property="phoneNumber"
                styleId="phoneNumber"/>
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
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="userFormEx.passwordHint"/>
        </th>
        <td>
            <html:text property="passwordHint"
                styleId="passwordHint" size="50"/>
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

<html:javascript formName="userFormEx" cdata="false"
      dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"
      src="<c:url value="/scripts/validator.jsp"/>"></script>