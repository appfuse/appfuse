<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="login.title"/></title>
    <meta name="heading" content="<fmt:message key='login.heading'/>"/>
    <meta name="menu" content="Login"/>
</head>
<body class="login">

<div class="span5">
    <form method="post" id="loginForm" action="<c:url value='/j_security_check'/>"
          onsubmit="saveUsername(this);return validateForm(this)" class="form-horizontal">
        <c:if test="${param.error != null}">
            <div class="alert alert-error fade in">
                <fmt:message key="errors.password.mismatch"/>
                <%--${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}--%>
            </div>
        </c:if>

        <fieldset class="control-group">
            <label for="j_username" class="required control-label">
                <fmt:message key="label.username"/> <span class="required">*</span>
            </label>

            <div class="controls">
                <input type="text" name="j_username" id="j_username" tabindex="1"/>
            </div>
        </fieldset>

        <fieldset class="control-group">
            <label for="j_password" class="required control-label">
                <fmt:message key="label.password"/> <span class="required">*</span>
            </label>

            <div class="controls">
                <input type="password" name="j_password" id="j_password" tabindex="2"/>
            </div>
        </fieldset>

        <c:if test="${appConfig['rememberMeEnabled']}">
            <fieldset class="control-group">
                <div class="controls">
                    <label class="checkbox">
                        <input type="checkbox" name="_spring_security_remember_me" id="rememberMe" tabindex="3"/>
                        <fmt:message key="login.rememberMe"/>
                    </label>
                </div>
            </fieldset>
        </c:if>

        <fieldset class="form-actions">
            <input type="submit" class="btn btn-primary" name="login" value="<fmt:message key='button.login'/>"
                   tabindex="4"/>
        </fieldset>
    </form>

    <c:set var="scripts" scope="request">
    <%@ include file="/scripts/login.js" %>
    </c:set>
</div>
<div class="span3">
    <p>
        <fmt:message key="login.signup">
            <fmt:param><c:url value="/signup"/></fmt:param>
        </fmt:message>
    </p>

    <p><fmt:message key="login.passwordHint"/></p>
</div>
</body>