<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="heading" content="<fmt:message key='userProfile.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<s:form name="userForm" action="saveUser" method="post" validate="true">
    <li style="display: none">
        <s:hidden key="user.id"/>
        <s:hidden key="user.version"/>
        <input type="hidden" name="from" value="${param.from}"/>

        <c:if test="${cookieLogin == 'true'}">
            <s:hidden key="user.password"/>
            <s:hidden key="user.confirmPassword"/>
        </c:if>

        <s:if test="user.version == null">
            <input type="hidden" name="encryptPass" value="true" />
        </s:if>
    </li>
    <li class="buttonBar right">
        <c:set var="buttons">
            <s:submit key="button.save" method="save" onclick="onFormSubmit(this.form)"/>
            
        <c:if test="${param.from == 'list' and not empty user.id}">
            <s:submit key="button.delete" method="delete" onclick="return confirmDelete('user')"/>
        </c:if>
        
            <s:submit key="button.cancel" method="cancel"/>
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

    <s:textfield key="user.username" cssClass="text large" required="true"/>

    <c:if test="${cookieLogin != 'true'}">
    <li>
        <div>
            <div class="left">
                <s:password key="user.password" showPassword="true" theme="xhtml" required="true" 
                    cssClass="text medium" onchange="passwordChanged(this)"/>
            </div>
            <div>
                <s:password key="user.confirmPassword" theme="xhtml" required="true" 
                    showPassword="true" cssClass="text medium" onchange="passwordChanged(this)"/>
            </div>
        </div>
    </li>
    </c:if>

    <s:textfield key="user.passwordHint" required="true" cssClass="text large"/>

    <li>
        <div>
            <div class="left">
                <s:textfield key="user.firstName" theme="xhtml" required="true" cssClass="text medium"/>
            </div>
            <div>
                <s:textfield key="user.lastName" theme="xhtml" required="true" cssClass="text medium"/>
            </div>
        </div>
    </li>

    <li>
        <div>
            <div class="left">
                <s:textfield key="user.email" theme="xhtml" required="true" cssClass="text medium"/>
            </div>
            <div>
                <s:textfield key="user.phoneNumber" theme="xhtml" cssClass="text medium"/>
            </div>
        </div>
    </li>

    <s:textfield key="user.website" required="true" cssClass="text large"/>

    <li>
        <label class="desc"><fmt:message key="user.address.address"/></label>
        <div class="group">
            <div>
                <s:textfield key="user.address.address" theme="xhtml" cssClass="text large" labelposition="bottom"/>
            </div>
            <div class="left">
                <s:textfield key="user.address.city" theme="xhtml" required="true" cssClass="text medium" 
                    labelposition="bottom"/>
            </div>
            <div>
                <s:textfield key="user.address.province" theme="xhtml" required="true" cssClass="text state" 
                    labelposition="bottom"/>
            </div>
            <div class="left">
                <s:textfield key="user.address.postalCode" theme="xhtml" required="true" cssClass="text medium" 
                    labelposition="bottom"/>
            </div>
            <div>
                <s:set name="country" value="user.address.country" scope="page"/>
                <appfuse:country name="user.address.country" prompt="" default="${country}"/>
                <p>
                    <label for="user.address.country">
                        <fmt:message key="user.address.country"/> <span class="req">*</span>
                    </label>
                </p>
            </div>
        </div>
    </li>
<c:choose>
    <c:when test="${param.from == 'list'}">
    <li>
        <fieldset>
            <legend><fmt:message key="userProfile.accountSettings"/></legend>
            <s:checkbox key="user.enabled" id="user.enabled" fieldValue="true" theme="simple"/>
            <label for="user.enabled" class="choice"><fmt:message key="user.enabled"/></label>

            <s:checkbox key="user.accountExpired" id="user.accountExpired" fieldValue="true" theme="simple"/>
            <label for="user.accountExpired" class="choice"><fmt:message key="user.accountExpired"/></label>

            <s:checkbox key="user.accountLocked" id="user.accountLocked" fieldValue="true" theme="simple"/>
            <label for="user.accountLocked" class="choice"><fmt:message key="user.accountLocked"/></label>

            <s:checkbox key="user.credentialsExpired" id="user.credentialsExpired" fieldValue="true" theme="simple"/>
            <label for="user.credentialsExpired" class="choice"><fmt:message key="user.credentialsExpired"/></label>
        </fieldset>
    </li>
    <li>
        <fieldset>
            <legend><fmt:message key="userProfile.assignRoles"/></legend>
            <table class="pickList">
                <tr>
                    <th class="pickLabel">
                        <label class="required"><fmt:message key="user.availableRoles"/></label>
                    </th>
                    <td></td>
                    <th class="pickLabel">
                        <label class="required"><fmt:message key="user.roles"/></label>
                    </th>
                </tr>
                <c:set var="leftList" value="${availableRoles}" scope="request"/>
                <s:set name="rightList" value="user.roleList" scope="request"/>
                <c:import url="/WEB-INF/pages/pickList.jsp">
                    <c:param name="listCount" value="1"/>
                    <c:param name="leftId" value="availableRoles"/>
                    <c:param name="rightId" value="userRoles"/>
                </c:import>
            </table>
        </fieldset>
    </li>
    </c:when>
    <c:otherwise>
    <li>
        <strong><fmt:message key="user.roles"/>:</strong>
        <s:iterator value="user.roleList" status="status">
          <s:property value="label"/><s:if test="!#status.last">,</s:if>
          <input type="hidden" name="userRoles" value="<s:property value="value"/>"/>
        </s:iterator>
        <s:hidden name="user.enabled" value="%{user.enabled}"/>
        <s:hidden name="user.accountExpired" value="%{user.accountExpired}"/>
        <s:hidden name="user.accountLocked" value="%{user.accountLocked}"/>
        <s:hidden name="user.credentialsExpired" value="%{user.credentialsExpired}"/>
    </li>
    </c:otherwise>
</c:choose>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</s:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms["userForm"]);
    highlightFormElements();

    function passwordChanged(passwordField) {
        if (passwordField.name == "user.password") {
            var origPassword = "<s:property value="user.password"/>";
        } else if (passwordField.name == "user.confirmPassword") {
            var origPassword = "<s:property value="user.confirmPassword"/>";
        }
        
        if (passwordField.value != origPassword) {
            createFormElement("input", "hidden",  "encryptPass", "encryptPass",
                              "true", passwordField.form);
        }
    }

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
<c:if test="${param.from == 'list'}">
    selectAll('userRoles');
</c:if>
}
</script>
