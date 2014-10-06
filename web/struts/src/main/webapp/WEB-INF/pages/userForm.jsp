<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="menu" content="UserMenu"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="userList.user"/></c:set>
<script type="text/javascript">var msgDelConfirm =
   "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<div class="col-sm-2">
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
<div class="col-sm-7">
    <s:form name="userForm" action="saveUser" method="post" validate="true" cssClass="well" autocomplete="off">
        <s:hidden key="user.id"/>
        <s:hidden key="user.version"/>
        <input type="hidden" name="from" value="${param.from}"/>

        <c:if test="${cookieLogin == 'true'}">
            <s:hidden key="user.password"/>
            <s:hidden key="user.confirmPassword"/>
        </c:if>
        <s:if test="user.version == null">
            <input type="hidden" name="encryptPass" value="true"/>
        </s:if>

        <s:textfield key="user.username" required="true" autofocus="true" cssClass="form-control"/>

    <c:if test="${cookieLogin != 'true'}">
        <div class="row">
            <div class="col-sm-6">
                <s:password key="user.password" showPassword="true" required="true"
                            onchange="passwordChanged(this)" cssClass="form-control"/>
            </div>
            <div class="col-sm-6">
                <s:password key="user.confirmPassword" required="true" cssClass="form-control"
                            showPassword="true" onchange="passwordChanged(this)"/>
            </div>
        </div>
    </c:if>

        <s:textfield key="user.passwordHint" required="true" cssClass="form-control"/>

        <div class="row">
            <div class="col-sm-6">
                <s:textfield key="user.firstName" required="true" cssClass="form-control"/>
            </div>
            <div class="col-sm-6">
                <s:textfield key="user.lastName" required="true" cssClass="form-control"/>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-6">
                <s:textfield key="user.email" required="true" cssClass="form-control"/>
            </div>
            <div class="col-sm-6">
                <s:textfield key="user.phoneNumber" cssClass="form-control"/>
            </div>
        </div>

        <s:textfield key="user.website" required="true" cssClass="form-control"/>

        <fieldset>
            <legend class="accordion-heading">
                <a data-toggle="collapse" href="#collapse-address"><fmt:message key="user.address.address"/></a>
            </legend>
            <div id="collapse-address" class="accordion-body collapse">
                <s:textfield key="user.address.address" cssClass="form-control"/>

                <div class="row">
                    <div class="col-sm-7">
                        <s:textfield key="user.address.city" cssClass="form-control"/>
                    </div>
                    <div class="col-sm-2">
                        <s:textfield key="user.address.province" cssClass="form-control"/>
                    </div>
                    <div class="col-sm-3">
                        <s:textfield key="user.address.postalCode" cssClass="form-control"/>
                    </div>
                </div>

                <s:set name="country" value="user.address.country" scope="page"/>
                <div class="form-group">
                    <label for="user.address.country">
                        <fmt:message key="user.address.country"/>
                    </label>

                    <appfuse:country name="user.address.country" prompt="" default="${country}"/>
                </div>
            </div>
        </fieldset>
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <fieldset class="form-group">
                    <label class="control-label"><fmt:message key="userProfile.accountSettings"/></label>
                    <label class="checkbox inline">
                        <s:checkbox key="user.enabled" id="user.enabled" theme="simple" fieldValue="true"/>
                        <fmt:message key="user.enabled"/>
                    </label>
                    <label class="checkbox inline">
                        <s:checkbox key="user.accountExpired" id="user.accountExpired" theme="simple"
                                    fieldValue="true"/>
                        <fmt:message key="user.accountExpired"/>
                    </label>
                    <label class="checkbox inline">
                        <s:checkbox key="user.accountLocked" id="user.accountLocked" theme="simple" fieldValue="true"/>
                        <fmt:message key="user.accountLocked"/>
                    </label>
                    <label class="checkbox inline">
                        <s:checkbox key="user.credentialsExpired" id="user.credentialsExpired" theme="simple"
                                    fieldValue="true"/>
                        <fmt:message key="user.credentialsExpired"/>
                    </label>
                </fieldset>
                <fieldset class="form-group">
                    <label for="userRoles" class="control-label"><fmt:message key="userProfile.assignRoles"/></label>
                    <select id="userRoles" name="userRoles" multiple="true" class="form-control">
                        <c:forEach items="${availableRoles}" var="role">
                            <option value="${role.value}" ${fn:contains(user.roles, role.label) ? 'selected' : ''}>${role.label}</option>
                        </c:forEach>
                    </select>
                </fieldset>
            </c:when>
            <c:otherwise>
                <div class="form-group">
                    <label class="control-label"><fmt:message key="user.roles"/>:</label>

                    <div class="controls readonly">
                        <s:iterator value="user.roleList" status="status">
                            <s:property value="label"/><s:if test="!#status.last">,</s:if>
                            <input type="hidden" name="userRoles" value="<s:property value="value"/>"/>
                        </s:iterator>
                    </div>
                    <s:hidden name="user.enabled" value="%{user.enabled}"/>
                    <s:hidden name="user.accountExpired" value="%{user.accountExpired}"/>
                    <s:hidden name="user.accountLocked" value="%{user.accountLocked}"/>
                    <s:hidden name="user.credentialsExpired" value="%{user.credentialsExpired}"/>
                </div>
            </c:otherwise>
        </c:choose>
        <div id="actions" class="form-group">
            <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
                <i class="icon-ok icon-white"></i>
                <fmt:message key="button.save"/>
            </s:submit>
            <c:if test="${param.from == 'list' and not empty user.id}">
                <s:submit type="button" cssClass="btn btn-danger" method="delete" key="button.delete"
                          onclick="return confirmMessage(msgDelConfirm)" theme="simple">
                    <i class="icon-trash"></i>
                    <fmt:message key="button.delete"/>
                </s:submit>
            </c:if>
            <s:submit type="button" cssClass="btn btn-default" method="cancel" key="button.cancel" theme="simple">
                <i class="icon-remove"></i>
                <fmt:message key="button.cancel"/>
            </s:submit>
        </div>
        </s:form>
    </div>

    <c:set var="scripts" scope="request">
    <script type="text/javascript">
        function passwordChanged(passwordField) {
            if (passwordField.name == "user.password") {
                var origPassword = "<s:property value="user.password"/>";
            } else if (passwordField.name == "user.confirmPassword") {
                var origPassword = "<s:property value="user.confirmPassword"/>";
            }

            if (passwordField.value != origPassword) {
                createFormElement("input", "hidden", "encryptPass", "encryptPass",
                        "true", passwordField.form);
            }
        }
    </script>
    </c:set>
