<script type="text/javascript">
<!--
    if (getCookie("username") != null) {
        document.getElementById("j_username").value = getCookie("username");
        document.getElementById("j_password").focus();
    } else {
        document.getElementById("j_username").focus();
    }
    
    function saveUsername(theForm) {
        var expires = new Date();
        expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.
        setCookie("username",theForm.j_username.value,expires,"<c:url value="/"/>");
    }
    
    function validateForm(form) {                                                               
        return validateRequired(form); 
    } 
    
    function passwordHint() {
        if (document.getElementById("j_username").value.length == 0) {
            alert("The <fmt:message key="label.username"/> field must be filled in to get a password hint sent to you.");
            document.getElementById("j_username").focus();
        } else {
            location.href="<c:url value="/passwordHint.html"/>?username=" + document.getElementById("j_username").value;     
        }
    }
    
    function required () { 
        this.aa = new Array("j_username", "<fmt:message key="label.username"/> is a required field.", new Function ("varName", " return this[varName];"));
        this.ab = new Array("j_password", "<fmt:message key="label.password"/> is a required field.", new Function ("varName", " return this[varName];"));
    } 

// -->
</script>