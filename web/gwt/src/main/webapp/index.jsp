<!DOCTYPE html>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
String contextPath = request.getContextPath();
pageContext.setAttribute("ctxPath", contextPath);
%>
<html lang="<%= LocaleContextHolder.getLocale() %>">
<head>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="rememberMeEnabled" content="${appConfig.rememberMeEnabled}" />
	<meta name="gwt:property" content="locale=<%= LocaleContextHolder.getLocale() %>">
    <link rel="icon" href="images/favicon.ico"/>
    <title><fmt:message key="webapp.name" /></title>

    <t:assets type="css"/>
</head>
<body>
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>

	<div id="loading" class="progress progress-info">
		<div id="progressbar" class="progress-bar" role="progressbar" style="width: 10%">Loading...</div>
	</div>

    <t:assets type="js"/>
  	<script type="text/javascript">$("#progressbar").css("width", "30%")</script>
    <script type="text/javascript" src="application/application.nocache.js"></script>
</body>
</html>
