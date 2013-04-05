<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="updatePassword.title"/></title>
    <meta name="menu" content="Login"/>
</head>
<body id="updatePassword">

<p><fmt:message key="updatePassword.message" /></p>

<form method="post" id="updatePassword" action="<c:url value='/updatePassword'/>" class="form-signin" autocomplete="off">
    <h2 class="form-signin-heading">
        <fmt:message key="updatePassword.heading"/>
    </h2>
    <input type="hidden" name="token" value="<c:out value="${token}" escapeXml="true" />" />
    <input type="text" name="username" id="username" class="input-block-level" 
    		value="<c:out value="${username}"  escapeXml="true" />"
           	placeholder="<fmt:message key="label.username"/>" required tabindex="1">
    <input type="password" class="input-block-level" name="password" id="password" tabindex="2"
           	placeholder="<fmt:message key="label.password"/>" required>

    <button type="submit" class="btn btn-large btn-primary" name="login" tabindex="4">
        <fmt:message key='button.save'/>
    </button>
</form>

</body>