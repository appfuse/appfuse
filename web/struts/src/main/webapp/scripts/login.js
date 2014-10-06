<script type="text/javascript">
    if ($.cookie("username") != null && $.cookie("username") != "") {
        $("#j_username").val($.cookie("username"));
        $("#j_password").focus();
    } else {
        $("#j_username").focus();
    }
    
    function saveUsername(theForm) {
        $.cookie("username",theForm.j_username.value, { expires: 30, path: "<c:url value="/"/>"});
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
            alert("<s:text name="errors.requiredField"><s:param><s:text name="label.username"/></s:param></s:text>");
            $("#j_username").focus();
        } else {
            location.href="<c:url value="/passwordHint"/>?username=" + $("#j_username").val();
        }
    }
    
    function required () { 
        this.aa = new Array("j_username", "<s:text name="errors.requiredField"><s:param><s:text name="label.username"/></s:param></s:text>", new Function ("varName", " return this[varName];"));
        this.ab = new Array("j_password", "<s:text name="errors.requiredField"><s:param><s:text name="label.password"/></s:param></s:text>", new Function ("varName", " return this[varName];"));
    }
</script>
