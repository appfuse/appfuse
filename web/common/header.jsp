<%@ include file="/common/taglibs.jsp"%>

<c:if test="${pageContext.request.locale.language != 'en'}">
    <div id="switchLocale"><a href="<c:url value='/mainMenu.html?locale=en'/>"><fmt:message key="webapp.name"/> in English</a></div>
</c:if>

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>

<div id="branding">
    <a href="<c:out value="${pageContext.request.contextPath}"/>">
        <img src="<c:url value="/images/logo.gif"/>" class="logo" alt="<fmt:message key="webapp.name"/>"/>
    </a>
</div>

<c:if test="${pageContext.request.requestURI != 'login.jsp' and pageContext.request.remoteUser != null}">
<div id="userStatus">
    <fmt:message key="user.status"/>
    <strong><authz:authentication operation="fullName"/></strong><br />
    <a href="<c:url value="/logout.jsp"/>"><fmt:message key="user.logout"/></a>
</div>
</c:if>

<c:if test="${applicationScope.userCounter != null}">
<div id="activeUsers">
    <authz:authorize ifAllGranted="admin">
        <a href="<c:url value="/activeUsers.html"/>"><fmt:message key="mainMenu.activeUsers"/></a>:
    </authz:authorize>
    <authz:authorize ifNotGranted="admin">
        <fmt:message key="mainMenu.activeUsers"/>:
    </authz:authorize>
    <c:if test="${userCounter >= 0}"><c:out value="${userCounter}"/></c:if>
</div>
</c:if>
