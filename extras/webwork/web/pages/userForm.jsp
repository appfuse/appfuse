<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
    <meta name="menu" content="UserMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<ww:form name="userForm" action="saveUser" method="post" validate="true">
<ww:hidden name="user.id" value="%{user.id}"/>
<ww:hidden name="user.version" value="%{user.version}"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>" />

<c:if test="${cookieLogin == 'true'}">
    <ww:hidden name="user.password" value="%{user.password}"/>
    <ww:hidden name="user.confirmPassword" value="%{user.confirmPassword}"/>
</c:if>

<ww:if test="user.username == null || user.username == ''">
    <input type="hidden" name="encryptPass" value="true" />
</ww:if>
    <li class="buttonBar right">
        <c:set var="buttons">
            <input type="submit" class="button" name="save"
                value="<fmt:message key="button.save"/>" onclick="onFormSubmit(this.form)"/>
            
        <c:if test="${param.from == 'list' and param.method != 'Add'}">
            <input type="submit" class="button" name="delete"
                value="<fmt:message key="button.delete"/>" onclick="return confirmDelete('user')}"/>
        </c:if>
        
            <input type="submit" class="button" name="method:cancel"
                value="<fmt:message key="button.cancel"/>" />
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
    <ww:textfield label="%{getText('user.username')}" name="user.username" 
        value="%{user.username}" cssClass="text large" required="true"/>

    <c:if test="${cookieLogin != 'true'}">
    <li>
        <div>
            <div class="left">
                <ww:password label="%{getText('user.password')}" name="user.password" show="true" theme="xhtml" value="%{user.password}"
                    required="true" cssClass="text medium" onchange="passwordChanged(this)"/>
            </div>
            <div>
                <ww:password label="%{getText('user.confirmPassword')}" name="user.confirmPassword" theme="xhtml" value="%{user.confirmPassword}"
                    required="true" show="true" cssClass="text medium"/>
            </div>
        </div>
    </li>
    </c:if>

    <ww:textfield label="%{getText('user.passwordHint')}" name="user.passwordHint"
        value="%{user.passwordHint}" required="true" cssClass="text large"/>

    <li>
        <div>
            <div class="left">
                <ww:textfield label="%{getText('user.firstName')}" name="user.firstName" theme="xhtml"
                    value="%{user.firstName}" required="true" cssClass="text medium"/>
            </div>
            <div>
                <ww:textfield label="%{getText('user.lastName')}" name="user.lastName" theme="xhtml"
                    value="%{user.lastName}" required="true" cssClass="text medium"/>
            </div>
        </div>
    </li>

    <li>
        <div>
            <div class="left">
                <ww:textfield label="%{getText('user.email')}" name="user.email" theme="xhtml"
                    value="%{user.email}" required="true" cssClass="text medium"/>
            </div>
            <div>
                <ww:textfield label="%{getText('user.phoneNumber')}" name="user.phoneNumber" theme="xhtml"
                    value="%{user.phoneNumber}" cssClass="text medium"/>
            </div>
        </div>
    </li>

    <ww:textfield label="%{getText('user.website')}" name="user.website"
        value="%{user.website}" required="true" cssClass="text large"/>

    <li>
        <label class="desc"><fmt:message key="user.address.address"/></label>
        <div class="group">
            <div>
                <ww:textfield label="%{getText('user.address.address')}" name="user.address.address"
                    value="%{user.address.address}" cssClass="text large" labelposition="bottom"/>
            </div>
            <div class="left">
                <ww:textfield label="%{getText('user.address.city')}" name="user.address.city"
                    value="%{user.address.city}" required="true" cssClass="text medium" labelposition="bottom"/>
            </div>
            <div>
                <ww:textfield label="%{getText('user.address.province')}" name="user.address.province"
                    value="%{user.address.province}" required="true" cssClass="text state" size="2" labelposition="bottom"/>
            </div>
            <div class="left">
                <ww:textfield label="%{getText('user.address.postalCode')}" name="user.address.postalCode"
                    value="%{user.address.postalCode}" required="true" cssClass="text zip" labelposition="bottom"/>
            </div>
            <div>
                <ww:set name="country" value="user.address.country" scope="page"/>
                <appfuse:country name="user.address.country" prompt="" default="${country}"/>
                <p><label for="user.address.country"><fmt:message key="user.address.country"/> <span class="req">*</span></p>
            </div>
        </div>
    </li>
<c:choose>
    <c:when test="${param.from == 'list' or param.method == 'Add'}">
    <li>
        <fieldset>
            <legend><fmt:message key="userProfile.accountSettings"/></legend>
            <ww:checkbox name="user.enabled" id="user.enabled"
                value="%{user.enabled}" fieldValue="true" theme="simple"/>
            <label for="user.enabled" class="choice"><fmt:message key="user.enabled"/></label>

            <ww:checkbox name="user.accountExpired" id="user.accountExpired"
                value="%{user.accountExpired}" fieldValue="true" theme="simple"/>
            <label for="user.accountExpired" class="choice"><fmt:message key="user.accountExpired"/></label>

            <ww:checkbox name="user.accountLocked" id="user.accountLocked"
                value="%{user.accountLocked}" fieldValue="true" theme="simple"/>
            <label for="user.accountLocked" class="choice"><fmt:message key="user.accountLocked"/></label>

            <ww:checkbox name="user.credentialsExpired" id="user.credentialsExpired"
                value="%{user.credentialsExpired}" fieldValue="true" theme="simple"/>
            <label for="user.credentialsExpired" class="choice"><fmt:message key="user.credentialsExpired"/></label>
        </fieldset>
    </li>
    <li>
        <fieldset>
            <legend><fmt:message key="userProfile.assignRoles"/></legend>
            <table class="pickList">
                <tr>
                    <th class="pickLabel">
                        <label class="required">
                            <fmt:message key="user.availableRoles"/>
                        </label>
                    </th>
                    <td></td>
                    <th class="pickLabel">
                        <label class="required">
                            <fmt:message key="user.roles"/>
                        </label>
                    </th>
                </tr>
                <c:set var="leftList" value="${availableRoles}" scope="request"/>
                <ww:set name="rightList" value="user.roleList" scope="request"/>
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
        <ww:iterator value="user.roleList" status="status">
          <ww:property value="label"/><ww:if test="!#status.last">,</ww:if>
          <input type="hidden" name="user.userRoles" value="<ww:property value="value"/>"/>
        </ww:iterator>
        <ww:hidden name="user.enabled" value="%{user.enabled}"/>
        <ww:hidden name="user.accountExpired" value="%{user.accountExpired}"/>
        <ww:hidden name="user.accountLocked" value="%{user.accountLocked}"/>
        <ww:hidden name="user.credentialsExpired" value="%{user.credentialsExpired}"/>
    </li>
    </c:otherwise>
</c:choose>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ww:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms["userForm"]);
    highlightFormElements();

    function passwordChanged(passwordField) {
        var origPassword = "<ww:property value="user.password"/>";
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
}
</script>
