<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%-- Include common set of tag library declarations for each layout --%>
<%@ include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <%-- Include common set of meta tags for each layout --%>
        <%@ include file="/common/meta.jsp" %>
        <title><fmt:message key="webapp.prefix"/><decorator:title/></title>
        <script type="text/javascript" src="<c:url value='/scripts/helptip.js'/>"></script>
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/default.css'/>" /> 
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/helptip.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/print.css'/>" />     
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script> 
      <c:if test="${sessionScope.currentUserForm != null}">
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/menuExpandable.css'/>" /> 
        <script type="text/javascript" src="<c:url value='/scripts/menuExpandable.js'/>"></script>
      </c:if>
        <decorator:head/>
        <style type="text/css" media="all">
            div.standardsNote {background: #FFFFCC; border: 1px solid blue; margin-bottom: 10px; padding: 5px}
        </style>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/>>

    <div class="standardsNote">
        <fmt:message key="errors.browser.warning"/>
    </div>

    <div id="screen">

        <div id="header">
            <% if (request.getRequestURL().indexOf("login.jsp") == -1) { %>
            <c:if test="${sessionScope.currentUserForm != null}">
                <div id="userStatus">
                    <fmt:message key="user.status"/>
                    <strong><c:out value="${currentUserForm.firstName}"/> 
                        <c:out value="${currentUserForm.lastName}"/></strong><br />
                    <a href="<c:url value="/logout.jsp"/>"><fmt:message key="user.logout"/></a>
                </div>
            </c:if>
            <% } %>
            <c:import url="/common/header.jsp"/>
        </div>

        <c:if test="${sessionScope.currentUserForm != null}">
            <c:import url="/WEB-INF/pages/menu.jsp"/>
        </c:if>

        <div id="content">
            <h1><decorator:getProperty property="page.heading"/></h1>
            <%@ include file="/common/messages.jsp" %>
            <decorator:body/>
        </div>
        
        <div id="footer">
            <c:import url="/common/footer.jsp"/>
        </div>
        
    </div>

</body>
</html>
