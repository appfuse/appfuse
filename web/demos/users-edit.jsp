<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp" %>
<%@ page import="java.util.*" %>

<html>
<head>
	<title>Editable Display Tag Table</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
    <link rel="stylesheet" type="text/css" media="all" 
        href="<c:url value="/styles/default.css"/>" />
    <script type="text/javascript"
        src="<c:url value="/scripts/global.js"/>"></script>
</head>

<body>
<div id="header"></div>
<c:import url="users-menu.jsp"/>

<div id="content">
<h1>Editable Display Tag Table</h1>

<p>
  This page is designed to demonstrate how you can use the display tag to create
  editable rows of data.  Most of the code in this page is JSP scriplets to
  do the Add/Update/Delete logic.  This implementation only supports editing 
  one record at a time.  
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

<%      
String method = request.getParameter("method");

if (method == null) {
    List testData = new ArrayList();
    Map map1 = new TreeMap();
    map1.put("id","1");
    map1.put("firstName","Bill");
    map1.put("lastName","Gates");
    testData.add(map1);
    
    Map map2 = new TreeMap();
    map2.put("id","2");
    map2.put("firstName","Scott");
    map2.put("lastName","McNealy");
    testData.add(map2);
    
    Map map3 = new TreeMap();
    map3.put("id","3");
    map3.put("firstName","Bill");
    map3.put("lastName","Joy");
    testData.add(map3);
    
    session.setAttribute( "test", testData);
} else {
    // grap the testDate from the session
    List testData = (List) session.getAttribute("test");
    String useMe = request.getParameter("id");
    if (useMe != null && method.equals("Delete")) {
        // figure out which object it is and delete it
        for (int i=0; i < testData.size(); i++) {
            Map m = (TreeMap) testData.get(i);
            String id = m.get("id").toString();
            if (useMe.equals(id)) {
                testData.remove(m);
                %><div class="message">Delete succeeded!</div><%
                break;
            }
        }
    } else if (useMe != null && method.equals("Save")) {
        // figure out which object it is and update it
        for (int i=0; i < testData.size(); i++) {
            Map m = (TreeMap) testData.get(i);
            String id = m.get("id").toString();
            if (useMe.equals(id)) {
                m.put("firstName", request.getParameter("firstName"));
                m.put("lastName", request.getParameter("lastName"));
                testData.set(i, m);
                %><div class="message">
                    <b><c:out value="${param.firstName} ${param.lastName}"/></b> updated successfully!
                  </div></h2><%
                break;
            }
        }
    } else if (method.equals("Add")) {
        Map map4 = new TreeMap();
        // generate a random number
        Random generator = new Random();
        String id = String.valueOf(generator.nextInt());
        pageContext.setAttribute("id", id);
        map4.put("id", id);
        map4.put("firstName", "");
        map4.put("lastName", "");
        testData.add(map4);
        %><c:redirect url="users-edit.jsp">
            <c:param name="id" value="${id}"/>
            <c:param name="method" value="Edit"/>
          </c:redirect>
        <%
    }
    session.setAttribute( "test", testData);
}
%>

<c:set var="checkAll">
    <input type="checkbox" name="allbox" onclick="checkAll(this.form)" style="margin: 0 0 0 4px" />
</c:set>
<form name="editForm" action="users-edit.jsp">
<c:if test="${not empty param.method}">
    <input type="button" onclick="location.href='users-edit.jsp'" class="button"
            value="Reset List" />
</c:if>
<c:if test="${param.method == 'Edit'}">
    <input type="submit" name="method" value="Save" class="button" />
</c:if>
<input type="submit" name="method" value="Edit" class="button"/>
<input type="button" name="method" value="Add" class="button" onclick="location.href='?method=Add'" />
<input type="submit" name="method" value="Delete" class="button" />
<br /><br />
<display:table name="${test}" id="test" class="list">
  <display:column width="5" title="${checkAll}">
    <input type="checkbox" name="id" value="<c:out value="${test.id}"/>" 
    <c:if test="${param.id == test.id and param.method != 'Save'}">checked="checked"</c:if>
        style="margin: 0 0 0 4px" onclick="radio(this)" />
  </display:column>
  <display:column title="First Name">
    <c:choose>
        <c:when test="${param.method == 'Edit' and param.id == test.id}">
            <input type="text" name="firstName" style="padding: 0"
                value="<c:out value="${test.firstName}"/>" />
        </c:when>
        <c:otherwise><c:out value="${test.firstName}"/></c:otherwise>
    </c:choose>
  </display:column>
  <display:column title="Last Name">
      <c:choose>
        <c:when test="${param.method == 'Edit' and param.id == test.id}">
            <input type="text" name="lastName" style="padding: 0"
                value="<c:out value="${test.lastName}"/>" />
        </c:when>
        <c:otherwise><c:out value="${test.lastName}"/></c:otherwise>
    </c:choose>
  </display:column>
</display:table>
</form>

<p style="margin-left: 10px">
<a href="?" onclick="toggleDisplay('source'); return false">
    View JSP Source</a>
</p>

<div id="source" style="display:none; margin-left: 10px; margin-top: 0">
<pre>
&lt;%      
String method = request.getParameter("method");

if (method == null) {
    List testData = new ArrayList();
    Map map1 = new TreeMap();
    map1.put("id","1");
    map1.put("firstName","Bill");
    map1.put("lastName","Gates");
    testData.add(map1);
    
    Map map2 = new TreeMap();
    map2.put("id","2");
    map2.put("firstName","Scott");
    map2.put("lastName","McNealy");
    testData.add(map2);
    
    Map map3 = new TreeMap();
    map3.put("id","3");
    map3.put("firstName","Bill");
    map3.put("lastName","Joy");
    testData.add(map3);
    
    session.setAttribute( "test", testData);
} else {
    // grap the testDate from the session
    List testData = (List) session.getAttribute("test");
    String useMe = request.getParameter("id");
    if (useMe != null &amp;&amp; method.equals("Delete")) {
        // figure out which object it is and delete it
        for (int i=0; i &lt; testData.size(); i++) {
            Map m = (TreeMap) testData.get(i);
            String id = m.get("id").toString();
            if (useMe.equals(id)) {
                testData.remove(m);
                %&gt;&lt;div class="message"&gt;Delete succeeded!&lt;/div&gt;&lt;%
                break;
            }
        }
    } else if (method.equals("Save")) {
        // figure out which object it is and update it
        for (int i=0; i &lt; testData.size(); i++) {
            Map m = (TreeMap) testData.get(i);
            String id = m.get("id").toString();
            if (useMe.equals(id)) {
                m.put("firstName", request.getParameter("firstName"));
                m.put("lastName", request.getParameter("lastName"));
                testData.set(i, m);
                %&gt;&lt;div class="message"&gt;
                    &lt;b&gt;&lt;c:out value="${param.firstName} ${param.lastName}"/&gt;&lt;/b&gt; updated successfully!
                  &lt;/div&gt;&lt;/h2&gt;&lt;%
                break;
            }
        }
    } else if (method.equals("Add")) {
        Map map4 = new TreeMap();
        // generate a random number
        Random generator = new Random();
        String id = String.valueOf(generator.nextInt());
        pageContext.setAttribute("id", id);
        map4.put("id", id);
        map4.put("firstName", "");
        map4.put("lastName", "");
        testData.add(map4);
        %&gt;&lt;c:redirect url="users-edit.jsp"&gt;
            &lt;c:param name="id" value="${id}"/&gt;
            &lt;c:param name="method" value="Edit"/&gt;
          &lt;/c:redirect&gt;
        &lt;%
    }
    session.setAttribute( "test", testData);
}
%&gt;

&lt;c:set var="checkAll"&gt;
    &lt;input type="checkbox" name="allbox" onclick="checkAll(this.form)" style="margin: 0 0 0 4px" /&gt;
&lt;/c:set&gt;
&lt;form name="editForm" action="users-edit.jsp"&gt;
&lt;c:if test="${not empty param.method}"&gt;
    &lt;input type="button" onclick="location.href='users-edit.jsp'" class="button"
            value="Reset List" /&gt;
&lt;/c:if&gt;
&lt;c:if test="${param.method == 'Edit'}"&gt;
    &lt;input type="submit" name="method" value="Save" class="button" /&gt;
&lt;/c:if&gt;
&lt;input type="submit" name="method" value="Edit" class="button"/&gt;
&lt;input type="button" name="method" value="Add" class="button" onclick="location.href='?method=Add'" /&gt;
&lt;input type="submit" name="method" value="Delete" class="button" /&gt;
&lt;br /&gt;&lt;br /&gt;
&lt;display:table name="${test}" id="test" class="list"&gt;
  &lt;display:column width="5" title="${checkAll}"&gt;
    &lt;input type="checkbox" name="id" value="&lt;c:out value="${test.id}"/&gt;" 
    &lt;c:if test="${param.id == test.id and param.method != 'Save'}"&gt;checked="checked"&lt;/c:if&gt;
        style="margin: 0 0 0 4px" onclick="radio(this)" /&gt;
  &lt;/display:column&gt;
  &lt;display:column title="First Name"&gt;
    &lt;c:choose&gt;
        &lt;c:when test="${param.method == 'Edit' and param.id == test.id}"&gt;
            &lt;input type="text" name="firstName" style="padding: 0"
                value="&lt;c:out value="${test.firstName}"/&gt;" /&gt;
        &lt;/c:when&gt;
        &lt;c:otherwise&gt;&lt;c:out value="${test.firstName}"/&gt;&lt;/c:otherwise&gt;
    &lt;/c:choose&gt;
  &lt;/display:column&gt;
  &lt;display:column title="Last Name"&gt;
      &lt;c:choose&gt;
        &lt;c:when test="${param.method == 'Edit' and param.id == test.id}"&gt;
            &lt;input type="text" name="lastName" style="padding: 0"
                value="&lt;c:out value="${test.lastName}"/&gt;" /&gt;
        &lt;/c:when&gt;
        &lt;c:otherwise&gt;&lt;c:out value="${test.lastName}"/&gt;&lt;/c:otherwise&gt;
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
