<%@ include file="/common/taglibs.jsp" %>

<head>
    <title>Data Access Error</title>
</head>

<p>
    <h2>Data Access Failure</h2>
    <c:out value="${requestScope.exception.message}"/>
</p>

<a href="home" onclick="history.back();return false">&#171; Back</a>
