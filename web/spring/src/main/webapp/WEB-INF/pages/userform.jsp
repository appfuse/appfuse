<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="menu" content="UserMenu"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="userList.user"/></c:set>
<script type="text/javascript">var msgDelConfirm =
   "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<div class="span2">
    <h2><fmt:message key="userProfile.heading"/></h2>
    <c:choose>
        <c:when test="${param.from == 'list'}">
            <p><fmt:message key="userProfile.admin.message"/></p>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="userProfile.message"/></p>
        </c:otherwise>
    </c:choose>
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

    <form:form commandName="user" method="post" action="userform" id="userForm" autocomplete="off"
               cssClass="well form-horizontal" onsubmit="return validateUser(this)">
        <form:hidden path="id"/>
        <form:hidden path="version"/>
        <input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>

        <c:if test="${cookieLogin == 'true'}">
            <form:hidden path="password"/>
            <form:hidden path="confirmPassword"/>
        </c:if>

        <c:if test="${empty user.version}">
            <input type="hidden" name="encryptPass" value="true"/>
        </c:if>

        <spring:bind path="user.username">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label styleClass="control-label" key="user.username"/>
            <div class="controls">
                <form:input path="username" id="username"/>
                <form:errors path="username" cssClass="help-inline"/>
            </div>
        </fieldset>
    <c:if test="${cookieLogin != 'true'}">
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
    </c:if>
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
<c:choose>
    <c:when test="${param.from == 'list' or param.method == 'Add'}">
        <fieldset class="control-group">
            <label class="control-label"><fmt:message key="userProfile.accountSettings"/></label>
            <div class="controls">
                <label class="checkbox inline">
                    <form:checkbox path="enabled" id="enabled"/>
                    <fmt:message key="user.enabled"/>
                </label>

                <label class="checkbox inline">
                    <form:checkbox path="accountExpired" id="accountExpired"/>
                    <fmt:message key="user.accountExpired"/>
                </label>

                <label class="checkbox inline">
                    <form:checkbox path="accountLocked" id="accountLocked"/>
                    <fmt:message key="user.accountLocked"/>
                </label>

                <label class="checkbox inline">
                    <form:checkbox path="credentialsExpired" id="credentialsExpired"/>
                    <fmt:message key="user.credentialsExpired"/>
                </label>
            </div>
        </fieldset>
        <fieldset class="control-group">
            <label for="userRoles" class="control-label"><fmt:message key="userProfile.assignRoles"/></label>
            <div class="controls">
                <select id="userRoles" name="userRoles" multiple="true">
                    <c:forEach items="${availableRoles}" var="role">
                    <option value="${role.value}" ${fn:contains(user.roles, role.label) ? 'selected' : ''}>${role.label}</option>
                    </c:forEach>
                </select>
            </div>
        </fieldset>
    </c:when>
    <c:when test="${not empty user.username}">
        <fieldset class="control-group">
            <label class="control-label"><fmt:message key="user.roles"/>:</label>
            <div class="controls readonly">
                <c:forEach var="role" items="${user.roleList}" varStatus="status">
                    <c:out value="${role.label}"/><c:if test="${!status.last}">,</c:if>
                    <input type="hidden" name="userRoles" value="<c:out value="${role.label}"/>"/>
                </c:forEach>
            </div>
            <form:hidden path="enabled"/>
            <form:hidden path="accountExpired"/>
            <form:hidden path="accountLocked"/>
            <form:hidden path="credentialsExpired"/>
        </fieldset>
    </c:when>
</c:choose>
        <fieldset class="form-actions">
            <button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false">
                <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
            </button>

            <c:if test="${param.from == 'list' and param.method != 'Add'}">
              <button type="submit" class="btn" name="delete" onclick="bCancel=true;return confirmMessage(msgDelConfirm)">
                  <i class="icon-trash"></i> <fmt:message key="button.delete"/>
              </button>
            </c:if>

            <button type="submit" class="btn" name="cancel" onclick="bCancel=true">
                <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
            </button>
        </fieldset>
    </form:form>
</div>

<c:set var="scripts" scope="request">
<script type="text/javascript">
    function passwordChanged(passwordField) {
        if (passwordField.id == "password") {
            var origPassword = "${user.password}";
        } else if (passwordField.id == "confirmPassword") {
            var origPassword = "${user.confirmPassword}";
        }

        if (passwordField.value != origPassword) {
            createFormElement("input", "hidden",  "encryptPass", "encryptPass",
                              "true", passwordField.form);
        }
    }

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    return validateUser(theForm);
}
</script>
</c:set>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

