<%@ include file="/common/taglibs.jsp"%>

<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<fmt:message key="signup.message"/>

<div class="separator"></div>

<form method="post" action="<c:url value="/signup.xml"/>" id="userForm"
    onsubmit="return validateUser(this)">
    
<table class="detail">
<c:set var="pageButtons">
    <tr>
    	<td></td>
    	<td class="buttonBar">
            <input type="submit" class="button" name="save" 
                onclick="bCancel=false" value="<fmt:message key="button.register"/>" />
                
            <input type="button" class="button" name="cancel"
                value="<fmt:message key="button.cancel"/>" 
                onclick="location.href='<c:url value="/"/>'" />
        </td>
    </tr>
</c:set>
    <tr>
        <th>
            <appfuse:label key="user.username"/>
        </th>
        <td>
        <spring:bind path="user.username">
            <input type="text" name="username" value="<c:out value="${status.value}"/>" id="username"/>
        </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.password"/>
        </th>
        <td>
            <spring:bind path="user.password">
            <input type="password" id="password" name="password" size="40" 
                value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.confirmPassword"/>
        </th>
        <td>
            <spring:bind path="user.confirmPassword">
            <input type="password" name="confirmPassword" id="confirmPassword"
                value="<c:out value="${status.value}"/>" size="40"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.firstName"/>
        </th>
        <td>
            <spring:bind path="user.firstName">
            <input type="text" name="firstName" value="<c:out value="${status.value}"/>" id="firstName"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.lastName"/>
        </th>
        <td>
            <spring:bind path="user.lastName">
            <input type="text" name="lastName" value="<c:out value="${status.value}"/>" id="lastName"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.address"/>
        </th>
        <td>
            <spring:bind path="user.address">
            <input type="text" name="address" value="<c:out value="${status.value}"/>" id="address" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.city"/>
        </th>
        <td>
            <spring:bind path="user.city">
            <input type="text" name="city" value="<c:out value="${status.value}"/>" id="city" size="40"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.province"/>
        </th>
        <td>
        <c:if test="${user.province != null}">
            <c:set var="state" value="${user.province}" scope="page"/>
            <state:state name="province" default="<%=(String)pageContext.getAttribute("state")%>"/>
        </c:if>
        <c:if test="${user.province == null}">
            <state:state name="province" prompt=""/>
        </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.country"/>
        </th>
        <td>
        <c:if test="${user.country != null}">
            <c:set var="country" value="${user.country}" scope="page"/>
            <country:country name="country" default="<%=(String)pageContext.getAttribute("country")%>"/>
        </c:if>
        <c:if test="${user.country == null}">
            <country:country name="country" prompt=""/>
        </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.postalCode"/>
        </th>
        <td>
            <spring:bind path="user.postalCode">
            <input type="text" name="postalCode" value="<c:out value="${status.value}"/>" id="postalCode" size="10"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.email"/>
        </th>
        <td>
            <spring:bind path="user.email">
            <input type="text" name="email" value="<c:out value="${status.value}"/>" id="email" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.phoneNumber"/>
        </th>
        <td>
            <spring:bind path="user.phoneNumber">
            <input type="text" name="phoneNumber" value="<c:out value="${status.value}"/>" id="phoneNumber"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.website"/>
        </th>
        <td>
            <spring:bind path="user.website">
            <input type="text" name="website" value="<c:out value="${status.value}"/>" id="website" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
            <c:if test="${!empty user.website}">
            <a href="<c:out value="${user.website}"/>"><fmt:message key="user.visitWebsite"/></a>
            </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="user.passwordHint"/>
        </th>
        <td>
            <spring:bind path="user.passwordHint">
            <input type="text" name="passwordHint" value="<c:out value="${status.value}"/>" id="passwordHint" size="50"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>

<%-- Print out buttons - defined at top of form --%>
<c:out value="${pageButtons}" escapeXml="false" />

</table>
</form>

<script type="text/javascript">
<!--
highlightFormElements();
var focusControl = document.forms["userForm"].elements["username"];

if (focusControl.type != "hidden" && !focusControl.disabled) {
   focusControl.focus();
}
</script>

<html:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript"
      src="<c:url value="/scripts/validator.jsp"/>"></script>


