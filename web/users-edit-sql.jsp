<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp" %>
<%@ page import="java.util.*" %>

<html>
<head>
	<title>Editable Table with SQL Tag</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
    <link rel="stylesheet" type="text/css" media="all" 
        href="<c:url value="/styles/default.css"/>" />
    <script type="text/javascript"
        src="<c:url value="/scripts/global.js"/>"></script>
</head>

<body>
<div id="header"></div>
<c:import url="/users-menu.jsp"/>

<div id="content">
<h1>Editable Table / SQL Tag</h1>

<p>
  This page is designed to demonstrate how you can use the display tag to create
  editable rows of data.  This particular example uses JSTL's &lt;sql:query&gt;
  tag to select, insert, update and delete rows from an actual database.
  <br /><br />
  Sorting is disabled on the column headings.  The reason?  There's
  <a href="http://sourceforge.net/tracker/index.php?func=detail&amp;aid=755120&amp;group_id=73068&amp;atid=536616">an issue</a>
  with sorting when your &lt;display:column&gt; tag has a body.  I didn't feel
  like digging in and fixing it.  Sorry. ;-)
</p>

<p style="margin-left: 10px; font-style: italic">
  Motivation for this exercise was gained from
  <a href="http://raibledesigns.com/display-edit">this implementation</a>, which
  is a patch that never made it into the old display tag (version 0.8.5).
</p>

<p>
Using the "View Table Source" link below this table, you can see the JSP/HTML
code that's used to render this table.
</p>

<%-- Create temporary table --%>
<c:if test="${empty applicationScope.tableCreated}">
<sql:transaction dataSource="jdbc/appfuse">

    <sql:update>
        DROP TABLE IF EXISTS temp_user
    </sql:update>
    <sql:update>
        CREATE TABLE temp_user (
           id BIGINT not null AUTO_INCREMENT,
           username VARCHAR(50),
           firstName VARCHAR(100),
           lastName VARCHAR(100),
           primary key (id)
        )
    </sql:update>

    <sql:update var="updateCount">
        INSERT INTO temp_user
            (username, firstName, lastName)
        VALUES
            ('mraible', 'Matt', 'Raible')
    </sql:update>
    <sql:update var="updateCount">
        INSERT INTO temp_user
            (username, firstName, lastName)
        VALUES
            ('tomcat', 'Tomcat', 'User')
    </sql:update>
</sql:transaction>
<c:set var="tableCreated" scope="application" value="true" />
</c:if>

<c:if test="${param.method == 'Add'}">
    <%-- This is a hack - if you use an auto-increment on your table,
         you won't have to worry about this kind of logic. --%>
    <%
    // generate a random number
    Random generator = new Random();
    String id = String.valueOf(generator.nextInt());
    pageContext.setAttribute("id", id);
    %>
    <sql:update dataSource="jdbc/appfuse">
        insert into temp_user (id) values (?)
        <sql:param value="${id}"/>
    </sql:update>
    <c:redirect url="/users-edit-sql.jsp">
        <c:param name="id" value="${id}"/>
        <c:param name="method" value="Edit"/>
    </c:redirect>
</c:if>

<c:if test="${param.method == 'Save'}">
    <sql:update dataSource="jdbc/appfuse">
        update temp_user set username=?, firstName=?, lastName=? where id=?
        <sql:param value="${param.username}"/>
        <sql:param value="${param.firstName}"/>
        <sql:param value="${param.lastName}"/>
        <sql:param value="${param.id}"/>
    </sql:update>
    <div class="message">
        <b><c:out value="${param.firstName} ${param.lastName}"/></b> updated successfully!
    </div>
</c:if>

<c:if test="${param.method == 'Delete'}">
    <sql:update dataSource="jdbc/appfuse">
        delete from temp_user where id=?
        <sql:param value="${param.id}"/>
    </sql:update>
    <div class="message">Delete succeeded!</div>
</c:if>
     
<sql:query var="users" dataSource="jdbc/appfuse">
    select id, username, firstName, lastName
    from temp_user order by upper(username);
</sql:query>

<c:set var="checkAll">
    <input type="checkbox" name="allbox" onclick="checkAll(this.form)" style="margin: 0 0 0 4px" />
</c:set>
<form name="editForm" action="users-edit-sql.jsp">
<c:if test="${not empty param.method}">
    <input type="button" onclick="location.href='users-edit-sql.jsp'" class="button"
            value="Cancel" />
</c:if>
<c:if test="${param.method == 'Edit'}">
    <input type="submit" name="method" value="Save" class="button" />
</c:if>
<input type="submit" name="method" value="Edit" class="button"/>
<input type="button" name="method" value="Add" class="button" onclick="location.href='?method=Add'" />
<input type="submit" name="method" value="Delete" class="button" />
<br /><br />
<display:table name="${users.rows}" id="user" class="list">
  <display:column width="5" title="${checkAll}">
    <input type="checkbox" name="id" value="<c:out value="${user.id}"/>" 
    <c:if test="${param.id == user.id and param.method != 'Save'}">checked="checked"</c:if>
        style="margin: 0 0 0 4px" onclick="radio(this)" />
  </display:column>
  <display:column title="Username">
    <c:choose>
        <c:when test="${param.method == 'Edit' and param.id == user.id}">
            <input type="text" name="username" style="padding: 0"
                value="<c:out value="${user.username}"/>" />
        </c:when>
        <c:otherwise><c:out value="${user.username}"/></c:otherwise>
    </c:choose>
  </display:column>
  <display:column title="First Name">
    <c:choose>
        <c:when test="${param.method == 'Edit' and param.id == user.id}">
            <input type="text" name="firstName" style="padding: 0"
                value="<c:out value="${user.firstName}"/>" />
        </c:when>
        <c:otherwise><c:out value="${user.firstName}"/></c:otherwise>
    </c:choose>
  </display:column>
  <display:column title="Last Name">
      <c:choose>
        <c:when test="${param.method == 'Edit' and param.id == user.id}">
            <input type="text" name="lastName" style="padding: 0"
                value="<c:out value="${user.lastName}"/>" />
        </c:when>
        <c:otherwise><c:out value="${user.lastName}"/></c:otherwise>
    </c:choose>
  </display:column>
</display:table>
</form>

<p style="margin-left: 10px">
<a href="?" onclick="toggleDisplay('source'); return false">
    View JSP Source</a>
</p>

<p style="margin-left: 10px; font-style: italic">
   NOTE: The table (temp_user) for this list will be dropped and re-created
   whenever this webapp is restarted.
</p>
<div id="source" style="display:none; margin-left: 10px; margin-top: 0">
<pre>
&lt;c:if test="${param.method == 'Add'}"&gt;
    &lt;%-- This is a hack - if you use an auto-increment on your table,
         you won't have to worry about this kind of logic. --%&gt;
    &lt;%
    // generate a random number
    Random generator = new Random();
    String id = String.valueOf(generator.nextInt());
    pageContext.setAttribute("id", id);
    %&gt;
    &lt;sql:update dataSource="jdbc/appfuse"&gt;
        insert into temp_user (id) values (?)
        &lt;sql:param value="${id}"/&gt;
    &lt;/sql:update&gt;
    &lt;c:redirect url="/users-edit-sql.jsp"&gt;
        &lt;c:param name="id" value="${id}"/&gt;
        &lt;c:param name="method" value="Edit"/&gt;
    &lt;/c:redirect&gt;
&lt;/c:if&gt;

&lt;c:if test="${param.method == 'Save'}"&gt;
    &lt;sql:update dataSource="jdbc/appfuse"&gt;
        update temp_user set username=?, firstName=?, lastName=? where id=?
        &lt;sql:param value="${param.username}"/&gt;
        &lt;sql:param value="${param.firstName}"/&gt;
        &lt;sql:param value="${param.lastName}"/&gt;
        &lt;sql:param value="${param.id}"/&gt;
    &lt;/sql:update&gt;
    &lt;div class="message"&gt;
        &lt;b&gt;&lt;c:out value="${param.firstName} ${param.lastName}"/&gt;&lt;/b&gt; updated successfully!
    &lt;/div&gt;
&lt;/c:if&gt;

&lt;c:if test="${param.method == 'Delete'}"&gt;
    &lt;sql:update dataSource="jdbc/appfuse"&gt;
        delete from temp_user where id=?
        &lt;sql:param value="${param.id}"/&gt;
    &lt;/sql:update&gt;
    &lt;div class="message"&gt;Delete succeeded!&lt;/div&gt;
&lt;/c:if&gt;

&lt;sql:query var="users" dataSource="jdbc/appfuse"&gt;
    select id, username, firstName, lastName
    from temp_user order by upper(username);
&lt;/sql:query&gt;

&lt;c:set var="checkAll"&gt;
    &lt;input type="checkbox" name="allbox" onclick="checkAll(this.form)" style="margin: 0 0 0 4px" /&gt;
&lt;/c:set&gt;
&lt;form name="editForm" action="users-edit-sql.jsp"&gt;
&lt;c:if test="${not empty param.method}"&gt;
    &lt;input type="button" onclick="location.href='users-edit-sql.jsp'" class="button"
            value="Cancel" /&gt;
&lt;/c:if&gt;
&lt;c:if test="${param.method == 'Edit'}"&gt;
    &lt;input type="submit" name="method" value="Save" class="button" /&gt;
&lt;/c:if&gt;
&lt;input type="submit" name="method" value="Edit" class="button"/&gt;
&lt;input type="button" name="method" value="Add" class="button" onclick="location.href='?method=Add'" /&gt;
&lt;input type="submit" name="method" value="Delete" class="button" /&gt;
&lt;br /&gt;&lt;br /&gt;
&lt;display:table name="${users.rows}" id="user" class="list"&gt;
  &lt;display:column width="5" title="${checkAll}"&gt;
    &lt;input type="checkbox" name="id" value="&lt;c:out value="${user.id}"/&gt;"
    &lt;c:if test="${param.id == user.id and param.method != 'Save'}"&gt;checked="checked"&lt;/c:if&gt;
        style="margin: 0 0 0 4px" onclick="radio(this)" /&gt;
  &lt;/display:column&gt;
  &lt;display:column title="Username"&gt;
    &lt;c:choose&gt;
        &lt;c:when test="${param.method == 'Edit' and param.id == user.id}"&gt;
            &lt;input type="text" name="username" style="padding: 0"
                value="&lt;c:out value="${user.username}"/&gt;" /&gt;
        &lt;/c:when&gt;
        &lt;c:otherwise&gt;&lt;c:out value="${user.username}"/&gt;&lt;/c:otherwise&gt;
    &lt;/c:choose&gt;
  &lt;/display:column&gt;
  &lt;display:column title="First Name"&gt;
    &lt;c:choose&gt;
        &lt;c:when test="${param.method == 'Edit' and param.id == user.id}"&gt;
            &lt;input type="text" name="firstName" style="padding: 0"
                value="&lt;c:out value="${user.firstName}"/&gt;" /&gt;
        &lt;/c:when&gt;
        &lt;c:otherwise&gt;&lt;c:out value="${user.firstName}"/&gt;&lt;/c:otherwise&gt;
    &lt;/c:choose&gt;
  &lt;/display:column&gt;
  &lt;display:column title="Last Name"&gt;
      &lt;c:choose&gt;
        &lt;c:when test="${param.method == 'Edit' and param.id == user.id}"&gt;
            &lt;input type="text" name="lastName" style="padding: 0"
                value="&lt;c:out value="${user.lastName}"/&gt;" /&gt;
        &lt;/c:when&gt;
        &lt;c:otherwise&gt;&lt;c:out value="${user.lastName}"/&gt;&lt;/c:otherwise&gt;
    &lt;/c:choose&gt;
  &lt;/display:column&gt;
&lt;/display:table&gt;
&lt;/form&gt;
</pre>
</div>
</div>

<div id="footer">
Can you think of a better way to do this?
Suggestions or Questions should be addressed to
<a href="mailto:displaytag-user@lists.sf.net">displaytag-user@lists.sf.net</a>.
</div>

</body>
</html>
