<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
    <meta name="menu" content="UserMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<form method="post" action="<c:url value="/editUser.html"/>" id="userForm" onsubmit="return onFormSubmit(this)">
<spring:bind path="user.id">
<input type="hidden" name="id" value="<c:out value="${status.value}"/>"/>
</spring:bind>
<spring:bind path="user.version">
<input type="hidden" name="version" value="<c:out value="${status.value}"/>"/>
</spring:bind>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>

<c:if test="${cookieLogin == 'true'}">
    <spring:bind path="user.password">
    <input type="hidden" name="password" value="<c:out value="${status.value}"/>"/>
    </spring:bind>
    <spring:bind path="user.confirmPassword">
    <input type="hidden" name="confirmPassword" value="<c:out value="${status.value}"/>"/>
    </spring:bind>
</c:if>

<c:if test="${empty user.username}">
    <input type="hidden" name="encryptPass" value="true"/>
</c:if>

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

        <c:if test="${param.from == 'list' and param.method != 'Add'}">
            <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('user')"
                value="<fmt:message key="button.delete"/>"/>
        </c:if>

            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <p><fmt:message key="userProfile.admin.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="userProfile.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="user.username"/>
        <spring:bind path="user.username">
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            <input type="text" name="username" value="<c:out value="${status.value}"/>" id="username" class="text large"/>
        </spring:bind>
    </li>
    <c:if test="${cookieLogin != 'true'}">
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="user.password"/>
                <spring:bind path="user.password">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <input type="password" id="password" name="password" class="text medium"
                        value="<c:out value="${status.value}"/>" onchange="passwordChanged(this)"/>
                </spring:bind>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="user.confirmPassword"/>
                <spring:bind path="user.confirmPassword">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <input type="password" name="confirmPassword" id="confirmPassword"
                        value="<c:out value="${status.value}"/>" class="text medium"/>
                </spring:bind>
            </div>
        </div>
    </li>
    </c:if>
    <li>
        <appfuse:label styleClass="desc" key="user.passwordHint"/>
        <spring:bind path="user.passwordHint">
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            <input type="text" name="passwordHint" value="<c:out value="${status.value}"/>" id="passwordHint" class="text large"/>
        </spring:bind>
    </li>
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="user.firstName"/>
            <spring:bind path="user.firstName">
                <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                <input type="text" name="firstName" value="<c:out value="${status.value}"/>" id="firstName" class="text medium" maxlength="50"/>
            </spring:bind>
        </div>
        <div>
            <appfuse:label styleClass="desc" key="user.lastName"/>
            <spring:bind path="user.lastName">
                <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                <input type="text" name="lastName" value="<c:out value="${status.value}"/>" id="lastName" class="text medium" maxlength="50"/>
            </spring:bind>
        </div>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="user.email"/>
                <spring:bind path="user.email">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <input type="text" name="email" value="<c:out value="${status.value}"/>" id="email" class="text medium"/>
                </spring:bind>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="user.phoneNumber"/>
                <spring:bind path="user.phoneNumber">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <input type="text" name="phoneNumber" value="<c:out value="${status.value}"/>" id="phoneNumber" class="text medium"/>
                </spring:bind>

            </div>
        </div>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="user.website"/>
        <spring:bind path="user.website">
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            <input type="text" name="website" value="<c:out value="${status.value}"/>" id="website" class="text large"/>
        </spring:bind>
    </li>
    <li>
        <label class="desc"><fmt:message key="user.address.address"/></label>
        <div class="group">
            <div>
                <spring:bind path="user.address.address">
                    <input type="text" name="address.address" value="<c:out value="${status.value}"/>" id="address.address" class="text large"/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
                <p><appfuse:label key="user.address.address"/></p>
            </div>
            <div class="left">
                <spring:bind path="user.address.city">
                    <input type="text" name="address.city" value="<c:out value="${status.value}"/>" id="address.city" class="text medium"/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
                <p><appfuse:label key="user.address.city"/></p>
            </div>
            <div>
                <spring:bind path="user.address.province">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <input type="text" name="address.province" value="<c:out value="${status.value}"/>" id="address.province" class="text state" size="2"/>
                </spring:bind>
                <p><appfuse:label key="user.address.province"/></p>
            </div>
            <div class="left">
                <spring:bind path="user.address.postalCode">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <input type="text" name="address.postalCode" value="<c:out value="${status.value}"/>" id="address.postalCode" class="text zip"/>
                </spring:bind>
                <p><appfuse:label key="user.address.postalCode"/></p>
            </div>
            <div>
                <spring:bind path="user.address.country">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <appfuse:country name="address.country" prompt="" default="${user.address.country}"/>
                </spring:bind>
                <p><appfuse:label key="user.address.country"/></p>
            </div>
        </div>
    </li>
<c:choose>
    <c:when test="${param.from == 'list' or param.method == 'Add'}">
    <li>
        <fieldset>
            <legend><fmt:message key="userProfile.accountSettings"/></legend>

            <spring:bind path="user.enabled">
                <input type="hidden" name="_<c:out value="${status.expression}"/>"  value="visible"/>
                <input type="checkbox" name="<c:out value="${status.expression}"/>"
                    <c:if test="${status.value}">checked="checked"</c:if> />
            </spring:bind>
            <label for="enabled" class="choice"><fmt:message key="user.enabled"/></label>

            <spring:bind path="user.accountExpired">
                <input type="hidden" name="_<c:out value="${status.expression}"/>"  value="visible"/>
                <input type="checkbox" name="<c:out value="${status.expression}"/>"
                    <c:if test="${status.value}">checked="checked"</c:if> />
            </spring:bind>
            <label for="accountExpired" class="choice"><fmt:message key="user.accountExpired"/></label>

            <spring:bind path="user.accountLocked">
                <input type="hidden" name="_<c:out value="${status.expression}"/>"  value="visible"/>
                <input type="checkbox" name="<c:out value="${status.expression}"/>"
                    <c:if test="${status.value}">checked="checked"</c:if> />
            </spring:bind>
            <label for="accountLocked" class="choice"><fmt:message key="user.accountLocked"/></label>

            <spring:bind path="user.credentialsExpired">
                <input type="hidden" name="_<c:out value="${status.expression}"/>"  value="visible"/>
                <input type="checkbox" name="<c:out value="${status.expression}"/>"
                    <c:if test="${status.value}">checked="checked"</c:if> />
            </spring:bind>
            <label for="credentialsExpired" class="choice"><fmt:message key="user.credentialsExpired"/></label>
        </fieldset>
    </li>
    <li>
        <fieldset class="pickList">
            <legend><fmt:message key="userProfile.assignRoles"/></legend>
            <table class="pickList">
                <tr>
                    <th class="pickLabel">
                        <appfuse:label key="user.availableRoles" colon="false" styleClass="required"/>
                    </th>
                    <td></td>
                    <th class="pickLabel">
                        <appfuse:label key="user.roles" colon="false" styleClass="required"/>
                    </th>
                </tr>
                <c:set var="leftList" value="${availableRoles}" scope="request"/>
                <c:set var="rightList" value="${user.roleList}" scope="request"/>
                <c:import url="/WEB-INF/pages/pickList.jsp">
                    <c:param name="listCount" value="1"/>
                    <c:param name="leftId" value="availableRoles"/>
                    <c:param name="rightId" value="userRoles"/>
                </c:import>
            </table>
        </fieldset>
    </li>
    </c:when>
    <c:when test="${not empty user.username}">
    <li>
        <strong><appfuse:label key="user.roles"/></strong>

        <c:forEach var="role" items="${user.roleList}" varStatus="status">
            <c:out value="${role.label}"/><c:if test="${!status.last}">,</c:if>
            <input type="hidden" name="userRoles" value="<c:out value="${role.label}"/>"/>
        </c:forEach>
        <spring:bind path="user.enabled">
            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
        </spring:bind>
        <spring:bind path="user.accountExpired">
            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
        </spring:bind>
        <spring:bind path="user.accountLocked">
            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
        </spring:bind>
        <spring:bind path="user.credentialsExpired">
            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
        </spring:bind>
    </li>
    </c:when>
</c:choose>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form>

<script type="text/javascript">
    Form.focusFirstElement($('userForm'));
    highlightFormElements();

    function passwordChanged(passwordField) {
        var origPassword = "<c:out value="${user.password}"/>";
        if (passwordField.value != origPassword) {
            createFormElement("input", "hidden",
                              "encryptPass", "encryptPass",
                              "true", passwordField.form);
        }
    }

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
<c:if test="${param.from == 'list'}">
    selectAll('userRoles');
</c:if>
    return validateUser(theForm);
}
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

