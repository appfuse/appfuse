<%@ include file="/common/taglibs.jsp"%>

<menu:useMenuDisplayer name="Velocity" config="topbarMenu.vm" permissions="rolesAdapter">
<ul class="nav">
    <c:if test="${empty pageContext.request.remoteUser}"><li class="active"><a href="<c:url value="/login"/>" class="current"><fmt:message key="login.title"/></a></li></c:if>
    <menu:displayMenu name="MainMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="Logout"/>
</ul>
</menu:useMenuDisplayer>
