<%@ include file="/common/taglibs.jsp"%>

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>

<c:if test="${applicationScope.userCounter != null}">
<div id="activeUsers">
  <logic-el:present role="${ADMIN_ROLE}">
    <html:link action="activeUsers"><fmt:message key="mainMenu.activeUsers"/></html:link>:
  </logic-el:present>
  <logic-el:notPresent role="${ADMIN_ROLE}">
    <fmt:message key="mainMenu.activeUsers"/>:
  </logic-el:notPresent>
  <c:if test="${userCounter >= 0}"><c:out value="${userCounter}"/></c:if>
</div>
</c:if>

<logic:present role="tomcat,admin">
    <html:link forward="mainMenu">
        <fmt:message key="mainMenu.title"/>
    </html:link>
</logic:present>

