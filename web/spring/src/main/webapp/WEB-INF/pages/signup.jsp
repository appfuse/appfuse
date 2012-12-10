<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="signup.title"/></title>
</head>

<body class="signup"/>

<div class="span2">
    <h2><fmt:message key="signup.heading"/></h2>
    <p><fmt:message key="signup.message"/></p>
</div>
<div class="span7">
    <spring:bind path="user.*">
        <c:if test="${not empty status.errorMessages}">
            <div class="alert alert-error fade in">
                <a href="#" data-dismiss="alert" class="close">&times;</a>
                <c:forEach var="error" items="${status.errorMessages}">
                    <c:out value="${error}" escapeXml="false"/><br/>
                </c:forEach>
            </div>
        </c:if>
    </spring:bind>

    <form:form commandName="user" method="post" action="signup" id="signupForm" autocomplete="off"
               cssClass="well form-horizontal" onsubmit="return validateUser(this)">
        <spring:bind path="user.username">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.username"/>
            <div class="controls">
                <form:input path="username" id="username"/>
                <form:errors path="username" cssClass="help-inline"/>
            </div>
        </fieldset>
        <spring:bind path="user.password">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.password"/>
            <div class="controls">
                <form:password path="password" id="password" showPassword="true"/>
                <form:errors path="password" cssClass="help-inline"/>
            </div>
        </fieldset>
        <spring:bind path="user.confirmPassword">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.confirmPassword"/>
            <div class="controls">
                <form:password path="confirmPassword" id="confirmPassword" showPassword="true"/>
                <form:errors path="confirmPassword" cssClass="help-inline"/>
            </div>
        </fieldset>
        <spring:bind path="user.passwordHint">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.passwordHint"/>
            <div class="controls">
                <form:input path="passwordHint" id="passwordHint"/>
                <form:errors path="passwordHint" cssClass="help-inline"/>
            </div>
        </fieldset>
        <spring:bind path="user.firstName">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.firstName"/>
            <div class="controls">
                <form:input path="firstName" id="firstName" maxlength="50"/>
                <form:errors path="firstName" cssClass="help-inline"/>
            </div>
        </fieldset>
        <spring:bind path="user.lastName">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.lastName"/>
            <div class="controls">
                <form:input path="lastName" id="lastName" maxlength="50"/>
                <form:errors path="lastName" cssClass="help-inline"/>
            </div>
        </fieldset>
        <spring:bind path="user.email">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.email"/>
            <div class="controls">
                <form:input path="email" id="email"/>
                <form:errors path="email" cssClass="help-inline"/>
            </div>
        </fieldset>
        <fieldset class="control-group">
            <appfuse:label styleClass="control-label" key="user.phoneNumber"/>
            <div class="controls">
                <form:input path="phoneNumber" id="phoneNumber"/>
            </div>
        </fieldset>
        <fieldset class="control-group">
            <appfuse:label styleClass="control-label" key="user.website"/>
            <div class="controls">
                <form:input path="website" id="website"/>
            </div>
        </fieldset>
        <fieldset>
            <legend class="accordion-heading">
                <a data-toggle="collapse" href="#collapse-address"><fmt:message key="user.address.address"/></a>
            </legend>
            <div id="collapse-address" class="accordion-body collapse">
                <fieldset class="control-group">
                    <appfuse:label styleClass="control-label" key="user.address.address"/>
                    <div class="controls">
                        <form:input path="address.address" id="address.address"/>
                    </div>
                </fieldset>
                <fieldset class="control-group">
                    <appfuse:label styleClass="control-label" key="user.address.city"/>
                    <div class="controls">
                        <form:input path="address.city" id="address.city"/>
                    </div>
                </fieldset>
                <fieldset class="control-group">
                    <appfuse:label styleClass="control-label" key="user.address.province"/>
                    <div class="controls">
                        <form:input path="address.province" id="address.province"/>
                    </div>
                </fieldset>
                <fieldset class="control-group">
                    <appfuse:label styleClass="control-label" key="user.address.postalCode"/>
                    <div class="controls">
                        <form:input path="address.postalCode" id="address.postalCode"/>
                    </div>
                </fieldset>
                <fieldset class="control-group">
                    <appfuse:label styleClass="control-label" key="user.address.country"/>
                    <div class="controls">
                        <appfuse:country name="address.country" prompt="" default="${user.address.country}"/>
                    </div>
                </fieldset>
            </div>
        </fieldset>
        <fieldset class="form-actions">
            <button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false">
                <i class="icon-ok icon-white"></i> <fmt:message key="button.register"/>
            </button>
            <button type="submit" class="btn" name="cancel" onclick="bCancel=true">
                <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
            </button>
        </fieldset>
    </form:form>
</div>

<c:set var="scripts" scope="request">
<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['signupForm']).focus();
    });
</script>
</c:set>