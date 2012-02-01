<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="signup.title"/></title>
</head>

<body class="signup"/>

<div class="span3">
    <h2><fmt:message key="signup.heading"/></h2>
    <fmt:message key="signup.message"/>
</div>
<div class="span7">
    <s:form name="signupForm" action="signup" method="post" validate="true" cssClass="well form-horizontal">

        <s:textfield key="user.username" required="true"/>
        <s:password key="user.password" showPassword="true" required="true"/>
        <s:password key="user.confirmPassword" required="true" showPassword="true"/>
        <s:textfield key="user.passwordHint" required="true"/>
        <s:textfield key="user.firstName" required="true"/>
        <s:textfield key="user.lastName" required="true"/>
        <s:textfield key="user.email" required="true"/>
        <s:textfield key="user.phoneNumber"/>
        <s:textfield key="user.website"/>

    <fieldset>
        <legend class="accordion-heading">
            <a data-toggle="collapse" href="#collapse-address"><fmt:message key="user.address.address"/></a>
        </legend>
        <div id="collapse-address" class="accordion-body collapse">
            <s:textfield key="user.address.address"/>
            <s:textfield key="user.address.city"/>
            <s:textfield key="user.address.province"/>
            <s:textfield key="user.address.postalCode"/>
            <s:set name="country" value="user.address.country" scope="page"/>
            <fieldset class="control-group">
                <label class="control-label" for="user.address.country">
                    <fmt:message key="user.address.country"/>
                </label>

                <div class="controls">
                    <appfuse:country name="user.address.country" prompt="" default="${country}"/>
                </div>
            </fieldset>
        </div>
    </fieldset>
    <fieldset class="form-actions">
        <s:submit key="button.register" cssClass="btn btn-primary" theme="simple"/>
        <s:submit key="button.cancel" name="cancel" cssClass="btn" theme="simple"/>
    </fieldset>
    </s:form>
</div>
