if (getCookie("username") != null && getCookie("username") != "") {
    $("j_username").value = getCookie("username");
    $("j_password").focus();
} else {
    $("j_username").focus();
}

function saveUsername(theForm) {
    var expires = new Date();
    expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.
    setCookie("username", theForm.j_username.value, expires, "/");
}

function passwordHint() {
    var prefix = "";
    if (document.location.toString().indexOf("error") > -1) {
        prefix = "../";
    }
    location.href = prefix + "passwordhint/" + $("j_username").value;
}