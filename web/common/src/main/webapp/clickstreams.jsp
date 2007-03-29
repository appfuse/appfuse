<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.*,com.opensymphony.clickstream.Clickstream" %>

<%
Map clickstreams = (Map) application.getAttribute("clickstreams");
String showbots = "false";

if (request.getParameter("showbots") != null)
{
    if (request.getParameter("showbots").equals("true"))
        showbots = "true";
    else if (request.getParameter("showbots").equals("both"))
        showbots = "both";
}
%>

<head>
    <title><fmt:message key="clickstreams.title"/></title>
    <meta name="heading" content="<fmt:message key='clickstreams.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<a href="?showbots=false">No Bots</a> |
<a href="?showbots=true">All Bots</a> |
<a href="?showbots=both">Both</a>

<p>
<% if (clickstreams.keySet().size() == 0) { %>
    No clickstreams in progress.
<% } %>

<%
Iterator it = clickstreams.keySet().iterator();
int count = 0;
while (it.hasNext())
{
    String key = (String)it.next();
    Clickstream stream = (Clickstream)clickstreams.get(key);

    if (showbots.equals("false") && stream.isBot())
    {
        continue;
    }
    else if (showbots.equals("true") && !stream.isBot())
    {
        continue;
    }

    count++;
    try {
%>

<%= count %>. <a href="viewstream.jsp?sid=<%= key %>"><b><%= (stream.getHostname() != null && !stream.getHostname().equals("") ? stream.getHostname() : "Stream") %></b></a> [<%= stream.getStream().size() %> reqs]<br />

<%
    }
    catch (Exception e)
    {
%>
    An error occurred - <%= e %><br />
<%
    }
}
%>
