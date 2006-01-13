<%@ include file="/common/taglibs.jsp"%>

<c:if test="${pageContext.request.locale.language != 'en'}">
    <div id="switchLocale"><a href="<c:url value='/mainMenu.html?locale=en'/>"><fmt:message key="webapp.name"/> in English</a></div>
</c:if>

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>

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

<c:if test="${pageContext.request.remoteUser != null}">
    <a href="<c:url value="/mainMenu.html"/>"><fmt:message key="mainMenu.title"/></a>
</c:if>

