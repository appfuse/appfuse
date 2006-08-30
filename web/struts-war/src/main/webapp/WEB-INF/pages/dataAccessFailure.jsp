<%@ include file="/common/taglibs.jsp" %>

<title>Data Access Error</title>
<content tag="heading">Data Access Failure</content>
<meta name="menu" content="AdminMenu"/>

<p>
    <c:out value="${requestScope.exception.message}"/>
</p>

<!--
<% 
Exception ex = (Exception) request.getAttribute("exception");
ex.printStackTrace(new java.io.PrintWriter(out)); 
%>
-->

<a href="mainMenu.html" onclick="history.back();return false">&#171; Back</a>
