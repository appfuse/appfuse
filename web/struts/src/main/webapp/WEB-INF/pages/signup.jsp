<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="signup.title"/></title>
</head>

<body class="signup"/>

<div class="span2">
    <h2><fmt:message key="signup.heading"/></h2>
    <fmt:message key="signup.message"/>
</div>
<div class="span7">
    <s:form name="signupForm" action="saveSignup" method="post" validate="true"
            autocomplete="off" cssClass="well form-horizontal">

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
    <div id="actions" class="form-actions">
        <s:submit type="button" cssClass="btn btn-primary" key="button.register" theme="simple">
            <i class="icon-ok icon-white"></i>
            <fmt:message key="button.register"/>
        </s:submit>
        <s:submit type="button" cssClass="btn" method="cancel" key="button.cancel" theme="simple">
            <i class="icon-remove"></i>
            <fmt:message key="button.cancel"/>
        </s:submit>
    </div>
    </s:form>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['signupForm']).focus();
    });
</script>
