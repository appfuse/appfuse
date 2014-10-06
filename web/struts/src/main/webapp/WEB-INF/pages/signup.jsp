<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="signup.title"/></title>
</head>

<body class="signup">

<div class="col-sm-2">
    <h2><fmt:message key="signup.heading"/></h2>
    <fmt:message key="signup.message"/>
</div>
<div class="col-sm-7">
    <s:form name="signupForm" action="saveSignup" method="post" validate="true"
            autocomplete="off" cssClass="well">

        <s:textfield key="user.username" required="true" autofocus="true" cssClass="form-control"/>

        <div class="row">
            <div class="col-sm-6">
                <s:password key="user.password" showPassword="true" required="true" cssClass="form-control"/>
            </div>
            <div class="col-sm-6">
                <s:password key="user.confirmPassword" required="true" showPassword="true" cssClass="form-control"/>
            </div>
        </div>

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

        <s:textfield key="user.website" cssClass="form-control"/>

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
                    <label class="control-label" for="user.address.country">
                        <fmt:message key="user.address.country"/>
                    </label>

                    <appfuse:country name="user.address.country" prompt="" default="${country}"/>
                </div>
            </div>
        </fieldset>
        <div id="actions" class="form-group form-actions">
            <s:submit type="button" cssClass="btn btn-primary" key="button.register" theme="simple">
                <i class="icon-ok icon-white"></i>
                <fmt:message key="button.register"/>
            </s:submit>
            <a href="./" class="btn btn-default"><i class="icon-remove"></i><fmt:message key="button.cancel"/></a>
        </div>
    </s:form>
</div>
</body>
