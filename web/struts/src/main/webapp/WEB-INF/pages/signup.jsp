<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="signup.title"/></title>
    <meta name="heading" content="<fmt:message key='signup.heading'/>"/>
</head>

<body id="signup"/>

<div class="separator"></div>

<s:form name="signupForm" action="signup" method="post" validate="true">
    <li class="info">
        <fmt:message key="signup.message"/>
    </li>

    <s:textfield key="user.username" cssClass="text large" required="true"/>

    <li>
        <div>
            <div class="left">
                <s:password key="user.password" showPassword="true" theme="xhtml" required="true" 
                    cssClass="text medium"/>
            </div>
            <div>
                <s:password key="user.confirmPassword" theme="xhtml" required="true" 
                    showPassword="true" cssClass="text medium"/>
            </div>
        </div>
    </li>

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
    <li class="buttonBar bottom">
        <s:submit key="button.register" cssClass="button"/>
        <s:submit key="button.cancel" name="cancel" cssClass="button"/>
    </li>
</s:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms["signupForm"]);
</script>
