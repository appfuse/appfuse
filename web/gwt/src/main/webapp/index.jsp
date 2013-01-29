<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
String contextPath = request.getContextPath();
pageContext.setAttribute("ctxPath", contextPath);
%>
<html lang="en">
<head>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="rememberMeEnabled" content="${appConfig.rememberMeEnabled}" />
    <link rel="icon" href="images/favicon.ico"/>
    <title><fmt:message key="webapp.name" /></title>

<%--
    <link rel="stylesheet" type="text/css" media="all" href="styles/lib/bootstrap-2.2.1.min.css" />
    <link rel="stylesheet" type="text/css" media="all" href="styles/lib/bootstrap-responsive-2.2.1.min.css" />
--%>
    <link rel="stylesheet" type="text/css" media="all" href="${ctxPath}/styles/style.css" />
    <script type="text/javascript" language="javascript" src="clientApp/clientApp.nocache.js"></script>
</head>
<body>
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>

	<div id="loading" class="progress progress-info">
		<div id="progressbar" class="bar" style="width: 0%">Loading...</div>
	</div>
  	
</body>
</html>
