<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="signup.title"/></title>
<content tag="heading"><fmt:message key="signup.heading"/></content>
<body id="signup"/>

<fmt:message key="signup.message"/>

<div class="separator"></div>

<ww:form name="'signup'" action="'signup'" method="'post'" validate="true">

    <ww:textfield label="getText('user.username')" name="'user.username'" 
        value="user.username" required="true"/>
    <ww:password label="getText('user.password')" name="'user.password'" 
        value="user.password" required="true" show="true" size="40"/>
    <ww:password label="getText('user.confirmPassword')" name="'user.confirmPassword'" 
        value="user.confirmPassword" required="true" show="true" size="40"/>
    <ww:textfield label="getText('user.firstName')" name="'user.firstName'"
        value="user.firstName" required="true"/>
    <ww:textfield label="getText('user.lastName')" name="'user.lastName'"
        value="user.lastName" required="true"/>
    <ww:textfield label="getText('user.address.address')" name="'user.address.address'"
        value="user.address.address" size="50"/>
    <ww:textfield label="getText('user.address.city')" name="'user.address.city'"
        value="user.address.city" size="40" required="true"/>
    <ww:textfield label="getText('user.address.province')" name="'user.address.province'"
        value="user.address.province" required="true" size="40"/>
    <tr>
        <th>
            <label for="user.address.country" class="required">
            	* <fmt:message key="user.address.country"/>:
            </label>
        </th>
        <td>
            <ww:set name="country" value="user.address.country" scope="page"/>
            <appfuse:country name="user.address.country" prompt="" default="${country}"/>
        </td>
    </tr>
    <ww:textfield label="getText('user.address.postalCode')" name="'user.address.postalCode'"
        value="user.address.postalCode" required="true" size="10"/>
    <ww:textfield label="getText('user.email')" name="'user.email'"
        value="user.email" required="true" size="50"/>
    <ww:textfield label="getText('user.phoneNumber')" name="'user.phoneNumber'"
        value="user.phoneNumber"/>
    <ww:textfield label="getText('user.website')" name="'user.website'"
        value="user.website" required="true" size="50"/>
    <ww:textfield label="getText('user.passwordHint')" name="'user.passwordHint'"
        value="user.passwordHint" required="true" size="50"/>
    <tr>
        <td></td>
        <td class="buttonBar">
            <input type="submit" class="button" name="save" 
               value="<fmt:message key="button.register"/>"/>
                
            <input type="submit" class="button" name="cancel"
                value="<fmt:message key="button.cancel"/>"/>
        </td>
    </tr>
</ww:form>

<script type="text/javascript">
    document.getElementById("user.username").focus();
</script>
