<%@ include file="/common/taglibs.jsp"%>

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>

<c:if test="${applicationScope.userCounter != null}">
<div id="activeUsers">
    <authz:authorize ifAllGranted="admin">
        <html:link action="activeUsers"><fmt:message key="mainMenu.activeUsers"/></html:link>:
    </authz:authorize>
    <authz:authorize ifNotGranted="admin">
        <fmt:message key="mainMenu.activeUsers"/>:
    </authz:authorize>
    <c:if test="${userCounter >= 0}"><c:out value="${userCounter}"/></c:if>
</div>
</c:if>

<c:if test="${pageContext.request.remoteUser != null}">
    <html:link forward="mainMenu">
        <fmt:message key="mainMenu.title"/>
    </html:link>
</c:if>

