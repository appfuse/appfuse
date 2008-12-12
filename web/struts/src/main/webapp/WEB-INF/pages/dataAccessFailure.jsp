<%@ include file="/common/taglibs.jsp" %>

<title>Data Access Error</title>

<head>
    <meta name="heading" content="Data Access Failure"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<p>
    <c:out value="${requestScope.exception.message}"/>
</p>

<!--
<% 
((Exception) request.getAttribute("exception")).printStackTrace(new java.io.PrintWriter(out)); 
%>
-->

<a href="mainMenu.html" onclick="history.back();return false">&#171; Back</a>
