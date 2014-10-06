<!DOCTYPE html>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
String contextPath = request.getContextPath();
pageContext.setAttribute("ctxPath", contextPath);
%>
<html lang="<%= LocaleContextHolder.getLocale() %>">
<head>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="rememberMeEnabled" content="${appConfig.rememberMeEnabled}" />
	<meta name="gwt:property" content="locale=<%= LocaleContextHolder.getLocale() %>">    
    <link rel="icon" href="images/favicon.ico"/>
    <title><fmt:message key="webapp.name" /></title>

	<c:set var="group" value="main" />
	<c:choose>
	    <c:when test="${param.debug}">
	        <link rel="stylesheet" type="text/css" href="${base}/webjars/bootswatch/3.0.0/spacelab/bootstrap.min.css"/>
	        <link rel="stylesheet" type="text/css" href="${base}/styles/style.css"/>
	    </c:when>
	    <c:otherwise>
	        <link rel="stylesheet" type="text/css" href="${base}/assets/v/${applicationScope.assetsVersion}/${group}.css"/>
	    </c:otherwise>
	</c:choose>
</head>
<body>
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>

	<div id="loading" class="progress progress-info">
		<div id="progressbar" class="progress-bar" role="progressbar" style="width: 10%">Loading...</div>
	</div>

	<c:choose>
	    <c:when test="${param.debug}">
	        <script type="text/javascript" src="${base}/webjars/jquery/1.9.0/jquery.min.js"></script>
	        <script type="text/javascript" src="${base}/webjars/bootstrap/3.0.2/js/bootstrap.min.js"></script>
	        <script type="text/javascript" src="${base}/webjars/jquery-cookie/1.3.1/jquery.cookie.js"></script>
	    </c:when>
	    <c:otherwise>
	        <script type="text/javascript" src="${base}/assets/v/${applicationScope.assetsVersion}/${group}.js"></script>
	    </c:otherwise>
	</c:choose>
  	
  	<script type="text/javascript">document.getElementById("progressbar").style.cssText("width: 30%")</script>
    <script type="text/javascript" language="javascript" src="script/script.nocache.js"></script>
</body>
</html>
