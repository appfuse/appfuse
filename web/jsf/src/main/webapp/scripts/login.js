<html xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html" xmlns:f="http://java.sun.com/jsf/core">
<ui:composition>
<script type="text/javascript">
    var usernameRequired = '<h:outputFormat value="#{text['errors.required']}"><f:param value="#{text['label.username']}"/></h:outputFormat>';
    var passwordRequired = '<h:outputFormat value="#{text['errors.required']}"><f:param value="#{text['label.password']}"/></h:outputFormat>';
//<![CDATA[
    if ($.cookie("username") != null && $.cookie("username") != "") {
        $("#j_username").val($.cookie("username"));
        $("#j_password").focus();
    } else {
        $("#j_username").focus();
    }

    function saveUsername(theForm) {
        $.cookie("username",theForm.j_username.value, { expires: 30, path: "${pageContext.request.contextPath}"});
    }

    function validateForm(form) {
        var valid = validateRequired(form);
        if (valid == false) {
            $(".form-group").addClass('error');
        }
        return valid;
    }
    
    function passwordHint() {
        if ($("#j_username").val().length == 0) {
            alert(usernameRequired);
            $("#j_username").focus();
        } else {
            location.href="${pageContext.request.contextPath}/passwordHint/" + $("#j_username").val();
        }
    }
    
    function required () { 
        this.aa = new Array("j_username", usernameRequired, new Function ("varName", " return this[varName];"));
        this.ab = new Array("j_password", passwordRequired, new Function ("varName", " return this[varName];"));
    }
//]]>
</script>
</ui:composition>
</html>