<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>SQL Tags Example</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/styles/default.css"/>" />
    <script type="text/javascript" src="<c:url value="/scripts/global.js"/>"></script>
</head>

<body>
<div id="header"></div>

<c:import url="users-menu.jsp"/>

<div id="content">
<h1>SQL Tags Example</h1>

<p>This page is designed to show how easy it is to list data from a database
using JSTL's SQL Tags.  The following is the query that is used to expose a
<em style="font-weight: bold">users</em> variable to the pageContext.
</p>
<pre>&lt;sql:query var="users" dataSource="jdbc/appfuse"&gt;
    select username, firstName, lastName
    from app_user order by upper(username);
&lt;/sql:query&gt;
</pre>
<p>
Using the "View Table Source" link below this table, you can see the JSP/HTML
code that's used to render this table.
</p>

<sql:query var="users" dataSource="jdbc/appfuse">
    select username, firstName, lastName
    from app_user order by upper(username);
</sql:query>

<table class="list">
<tr>
    <th><bean:message key="user.username"/></th>
    <th><bean:message key="user.firstName"/></th>
    <th><bean:message key="user.lastName"/></th>
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
<a href="?" onclick="toggleDisplay('sqlSource'); return false">
    View Table Source</a>
</p>

<div id="sqlSource" style="display:none; margin-left: 10px; margin-top: 0">
<pre>&lt;table class="list"&gt;
&lt;tr&gt;
    &lt;th&gt;&lt;bean:message key="user.username"/&gt;&lt;/th&gt;
    &lt;th&gt;&lt;bean:message key="user.firstName"/&gt;&lt;/th&gt;
    &lt;th&gt;&lt;bean:message key="user.lastName"/&gt;&lt;/th&gt;
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

<p>So that's cool, right?  But how about something even better?!  The 
<a href="http://displaytag.sf.net">display tag</a> <strong>now supports</strong>
iterating this set of results.  All you need to do is reference the ${users.row}
in the <em>name</em> attribute when using the EL tag. Now you can render 
this same data set, but this time you get column sorting.
</p>

<display:table name="${users.rows}" id="user" class="list">
    <display:column property="username" sortable="true" titleKey="user.username"/>
    <display:column property="firstName" sortable="true" titleKey="user.firstName"/>
    <display:column property="lastName" sortable="true" titleKey="user.lastName"/>
</display:table>

<p style="margin-left: 10px">
<a href="?" onclick="toggleDisplay('displaySource'); return false">
    View Table Source</a>
</p>

<div id="displaySource" style="display: none; margin-left: 10px; margin-top: 0">
<pre>&lt;display:table name="${users.rows}" id="user" class="list"&gt;
    &lt;display:column property="username" sortable="true" titleKey="user.username"/&gt;
    &lt;display:column property="firstName" sortable="true" titleKey="user.firstName"/&gt;
    &lt;display:column property="lastName" sortable="true" titleKey="user.lastName"/&gt;
&lt;/display:table&gt;
</pre>
</div>

<p>Heck, it even supports displaying all columns returned. 
If you use <code>&lt;display:table name="${users.rows}" class="list"/&gt;</code> 
- it'll render what you see below.
</p>

<display:table name="${users.rows}" class="list"/>

<p>Now that you're into it - checkout the other examples I put together:</p>
<ul>
    <li><a href="users-edit.jsp">Editable Display Tag Table</a></li>
    <li><a href="users-edit-sql.jsp">Editable Table / SQL</a></li>
</ul>
</div>

<div id="footer">
Suggestions or Questions should be addressed to 
<a href="mailto:displaytag-user@lists.sf.net">displaytag-user@lists.sf.net</a>.
</div>
</body>
</html>
