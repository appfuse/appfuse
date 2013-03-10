function initDataOnLoad() {
    if ($.cookie("username") != null && $.cookie("username") != "") {
        $("#j_username").val($.cookie("username"));
        $("#j_password").focus();
    } else {
        $("#j_username").focus();
    }
}

function saveUsername(theForm) {
    $.cookie("username",theForm.j_username.value, { expires: 30, path: "/"});
}

function passwordHint() {
    var prefix = "";
    if (document.location.toString().indexOf("error") > -1) {
        prefix = "../";
    }
    location.href = prefix + "passwordHint/" + $("#j_username").val();
}
