<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<%-- Include common set of tag library declarations for each layout --%>
<%@ include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <%-- Include common set of meta tags for each layout --%>
        <%@ include file="/common/meta.jsp" %>
        <title><decorator:title/> | <fmt:message key="webapp.name"/></title>
        
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/default.css'/>" /> 
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/helptip.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/print.css'/>" />    

        <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script> 
        <script type="text/javascript" src="<c:url value='/scripts/scriptaculous.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/helptip.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>

      <c:if test="${pageContext.request.remoteUser != null}">
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/coolmenu.css'/>" /> 
        <script type="text/javascript" src="<c:url value='/scripts/coolmenu4.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/cm_addins.js'/>"></script>
      </c:if>
        <decorator:head/>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/>>
    <a href="<c:out value="${pageContext.request.contextPath}"/>">
        <img src="<c:url value="/images/logo.gif"/>" class="logo" alt="<fmt:message key="webapp.name"/>"/>
    </a>
    
    <%-- Must come after body to work in IE --%>    
    <script type="text/javascript" src="<c:url value='/scripts/coolmenu4-config.js'/>"></script>

    <div id="screen">

        <div id="header">
            <% if (request.getRequestURL().indexOf("login.jsp") == -1) { %>
            <c:if test="${pageContext.request.remoteUser != null}">
                <div id="userStatus">
                    <fmt:message key="user.status"/>
                    <strong><authz:authentication operation="fullName"/></strong><br />
                    <a href="<c:url value="/logout.jsp"/>"><fmt:message key="user.logout"/></a>
                </div>
            </c:if>
            <% } %>
            <jsp:include page="/common/header.jsp"/>
        </div>

        <c:if test="${pageContext.request.remoteUser != null}">
            <jsp:include page="/menu.jsp"/>
        </c:if>

        <div id="content">
            <h1><decorator:getProperty property="page.heading"/></h1>
            <%@ include file="/common/messages.jsp" %>
            <decorator:body/>
        </div>
        
        <div id="footer">
            <jsp:include page="/common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
