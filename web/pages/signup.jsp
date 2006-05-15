<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="signup.title"/></title>
<content tag="heading"><fmt:message key="signup.heading"/></content>
<body id="signup"/>

<html:form action="/signup" styleId="userForm" onsubmit="return validateUserForm(this)">
<ul>
    <li class="info">
       <fmt:message key="signup.message"/>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="userForm.username"/>
        <html:errors property="username"/>
        <html:text styleClass="text large" property="username" styleId="username"/>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="userForm.password"/>
                <html:errors property="password"/>
                <html:password styleClass="text medium" property="password" styleId="password" redisplay="true"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="userForm.confirmPassword"/>
                <html:errors property="confirmPassword"/>
                <html:password styleClass="text medium" property="confirmPassword" styleId="confirmPassword" redisplay="true"/>
            </div>
        </div>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="userForm.passwordHint"/>
        <html:errors property="passwordHint"/>
        <html:text styleClass="text large" property="passwordHint" styleId="passwordHint" size="50"/>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="userForm.firstName"/>
                <html:errors property="firstName"/>
                <html:text styleClass="text medium" property="firstName" styleId="firstName" maxlength="50"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="userForm.lastName"/>
                <html:errors property="lastName"/>
                <html:text styleClass="text" property="lastName" styleId="lastName" maxlength="50"/>
            </div>
        </div>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="userForm.email"/>
                <html:errors property="email"/>
                <html:text styleClass="text medium" property="email" styleId="email" size="50"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="userForm.phoneNumber"/>
                <html:errors property="phoneNumber"/>
                <html:text styleClass="text medium" property="phoneNumber" styleId="phoneNumber"/>
            </div>
        </div>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="userForm.website"/>
        <html:errors property="website"/>
        <html:text styleClass="text large" property="website" styleId="website" size="50"/>
    </li>
    <li>
        <label class="desc"><fmt:message key="userForm.addressForm.address"/></label>
        <div class="group">
            <div>
                <html:text styleClass="text large" property="addressForm.address"
                    styleId="addressForm.address"/>
                <html:errors property="addressForm.address"/>
                <p><appfuse:label key="userForm.addressForm.address"/></p>
            </div>
            <div class="left">
                <html:text styleClass="text medium" property="addressForm.city"
                    styleId="addressForm.city"/>
                <html:errors property="addressForm.city"/>
                <p><appfuse:label key="userForm.addressForm.city"/></p>
            </div>
            <div>
                <html:text styleClass="text state" property="addressForm.province"
                    styleId="addressForm.province" size="2"/>
                <html:errors property="addressForm.province"/>
                <p><appfuse:label key="userForm.addressForm.province"/></p>
            </div>
            <div class="left">
                <html:text styleClass="text zip" property="addressForm.postalCode"
                    styleId="addressForm.postalCode"/>
                <html:errors property="addressForm.postalCode"/>
                <p><appfuse:label key="userForm.addressForm.postalCode"/></p>
            </div>
            <div>
                <appfuse:country name="countries" toScope="page"/>
                <html:select property="addressForm.country" styleClass="select">
                    <html:option value=""/>
                    <html:options collection="countries"
                        property="value" labelProperty="label"/>
                </html:select>
                <html:errors property="addressForm.country"/>
                <p><appfuse:label key="userForm.addressForm.country"/></p>
            </div>
        </div>
    </li>
    <li class="buttonBar bottom">
        <html:submit styleClass="button" onclick="bCancel=false">
            <fmt:message key="button.register"/>
        </html:submit>

        <html:cancel styleClass="button" onclick="bCancel=true">
            <fmt:message key="button.cancel"/>
        </html:cancel>
    </li>
</ul>
</html:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms['userForm']);
</script>
<html:javascript formName="userForm" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>