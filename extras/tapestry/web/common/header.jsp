<%@ include file="/common/taglibs.jsp"%>

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>

<c:if test="${applicationScope.userCounter != null}">
<div id="activeUsers">
    <r:isUserInRole role="admin">
        <a href="<c:url value="/activeUsers.html"/>"><fmt:message key="mainMenu.activeUsers"/></a>:
    </r:isUserInRole>
    <r:isUserInRole role="admin" value="false">
        <fmt:message key="mainMenu.activeUsers"/>:
    </r:isUserInRole>
    <c:if test="${userCounter >= 0}"><c:out value="${userCounter}"/></c:if>
</div>
</c:if>

<c:if test="${sessionScope.currentUserForm != null}">
    <a href="<c:url value="/mainMenu.html"/>"><fmt:message key="mainMenu.title"/></a>
</c:if>
