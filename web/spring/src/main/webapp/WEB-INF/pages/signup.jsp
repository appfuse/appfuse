<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="signup.title"/></title>
<content tag="heading"><fmt:message key="signup.heading"/></content>
<body id="signup"/>

<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">    
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<div class="separator"></div>

<form method="post" action="<c:url value="/signup.html"/>" id="signupForm" onsubmit="return validateUser(this)">
<ul>
    <li class="info">
        <fmt:message key="signup.message"/>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="user.username"/>
        <spring:bind path="user.username">
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            <input type="text" name="username" value="<c:out value="${status.value}"/>" id="username" class="text large"/>
        </spring:bind>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="user.password"/>
                <spring:bind path="user.password">
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                    <input type="password" id="password" name="password" class="text medium"
                        value="<c:out value="${status.value}"/>"/>
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
    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.register"/>"/>
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
    </li>
</ul>
</form>

<script type="text/javascript">
    Form.focusFirstElement($('signupForm'));
    highlightFormElements();
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>


