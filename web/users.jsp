<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>

<html>
<head>
	<title>SQL Tags Example</title>
    <link rel="stylesheet" type="text/css" media="all" 
        href="<c:url value="/styles/default.css"/>" />
    <script type="text/javascript"
        src="<c:url value="/scripts/global.js"/>"></script>
</head>

<body>
<div id="header"></div>

<div id="content">
<h1>User List with JSTL's SQL Tags</h1>

<sql:query var="users" dataSource="jdbc/appfuse">
    select username, firstName, lastName
    from app_user order by upper(username);
</sql:query>

<table class="list">
<tr>
    <th><bean:message key="userFormEx.username"/></th>
    <th><bean:message key="userFormEx.firstName"/></th>
    <th><bean:message key="userFormEx.lastName"/></th>
</tr>

<c:forEach var="row" items="${users.rows}" varStatus="status">
<c:choose>
<c:when test="${status.count % 2 == 0}"><tr class="even"></c:when>
<c:otherwise><tr class="odd"></c:otherwise>
</c:choose>
    <td><c:out value="${row.username}"/></td>
    <td><c:out value="${row.firstName}"/></td>
    <td><c:out value="${row.lastName}"/></td>
</tr>
</c:forEach>
</table>

<p style="margin-left: 10px">
<a href="?" onclick="toggleDisplay('sqlSource'); return false">View Source</a>
</p>

<div id="sqlSource" style="display:none; margin-left: 10px">
<pre>&lt;sql:query var="users" dataSource="jdbc/appfuse"&gt;
    select username, firstName, lastName
    from app_user order by upper(username);
&lt;/sql:query&gt;

&lt;table class="list"&gt;
&lt;tr&gt;
    &lt;th&gt;&lt;bean:message key="userFormEx.username"/&gt;&lt;/th&gt;
    &lt;th&gt;&lt;bean:message key="userFormEx.firstName"/&gt;&lt;/th&gt;
    &lt;th&gt;&lt;bean:message key="userFormEx.lastName"/&gt;&lt;/th&gt;
&lt;/tr&gt;

&lt;c:forEach var="row" items="${users.rows}" varStatus="status"&gt;
&lt;c:choose&gt;
&lt;c:when test="${status.count % 2 == 0}"&gt;&lt;tr class="even"&gt;&lt;/c:when&gt;
&lt;c:otherwise&gt;&lt;tr class="odd"&gt;&lt;/c:otherwise&gt;
&lt;/c:choose&gt;
    &lt;td&gt;&lt;c:out value="${row.username}"/&gt;&lt;/td&gt;
    &lt;td&gt;&lt;c:out value="${row.firstName}"/&gt;&lt;/td&gt;
    &lt;td&gt;&lt;c:out value="${row.lastName}"/&gt;&lt;/td&gt;
&lt;/tr&gt;
&lt;/c:forEach&gt;
&lt;/table&gt;
</pre>
</div>


<display:table name="users.rows"/>
</div>

<div id="footer"></div>
</body>
</html>
