<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%-- Include common set of tag library declarations for each layout --%>
<%@ include file="/common/taglibs.jsp"%>

<html:html xhtml="true" locale="true">
	<head>
        <%-- Include common set of meta tags for each layout --%>
        <%@ include file="/common/meta.jsp" %>
        
		<%-- Push tiles attributes in page context --%>
		<tiles:importAttribute />
		<title>
            <fmt:message key="webapp.prefix"/>
            <fmt:message key="${titleKey}"/>
        </title>
		
    <%-- Get Javascript List --%>
    <tiles:useAttribute id="scriptList" name="scripts" 
        classname="java.util.List" ignore="true"/>
    
    <c:forEach var="js" items="${scriptList}">
        <script type="text/javascript"
            src="<c:url value="${js}"/>"></script>
    </c:forEach>
    
    <%-- Get List of Stylesheets --%>
    <tiles:useAttribute id="styleList" name="styles" 
        classname="java.util.List" ignore="true"/>
    
    <c:forEach var="css" items="${styleList}">
        <link rel="stylesheet" type="text/css" media="all" 
            href="<c:url value="${css}"/>" /> 
    </c:forEach>

    <%-- Get List of Print Stylesheets --%>
    <tiles:useAttribute id="printList" name="printStyles" 
        classname="java.util.List" ignore="true"/>
        
    <c:forEach var="css" items="${printList}">
        <link rel="stylesheet" type="text/css" media="print" 
            href="<c:url value="${css}"/>" /> 
    </c:forEach>
    
        <style type="text/css" media="all">
            div.standardsNote {background: #FFFFCC; border: 1px solid blue; margin-bottom: 10px; padding: 5px}
        </style>
	</head>
    
    <c:set var="bodyId" scope="request">
        <tiles:getAsString name="body.id" ignore="true"/>
    </c:set>

<body<c:if test="${not empty bodyId}"> id="<c:out value="${bodyId}"/>"</c:if>>

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
                    <html:link forward="logout">
                        <fmt:message key="user.logout"/>
                    </html:link>
                </div>
            </c:if>
            <% } %>
            <tiles:insert attribute="header"/>
        </div>
        
        <tiles:insert attribute="menu" ignore="true"/>
        
        <div id="content">
            <h1><fmt:message key="${headingKey}"/></h1>
            <%@ include file="/common/messages.jsp" %>
            <tiles:insert attribute="content"/>
        </div>
        
        <div id="footer">
            <tiles:insert attribute="footer"/>
        </div>
        
    </div>

</body>
</html:html>

