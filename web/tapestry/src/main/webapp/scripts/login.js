if ($j.cookie("username") != null && $j.cookie("username") != "") {
    $j("#j_username").val($j.cookie("username"));
    $j("#j_password").focus();
} else {
    $j("#j_username").focus();
    $("#j_username").focus();
}

function saveUsername(theForm) {
    $j.cookie("username",theForm.j_username.value, { expires: 30, path: "/"});
}

function passwordHint() {
    var prefix = "";
    if (document.location.toString().indexOf("error") > -1) {
        prefix = "../";
    }
    location.href = prefix + "passwordhint/" + $j("#j_username").val();
}


function requestRecoveryToken() {
    var prefix = "";
    if (document.location.toString().indexOf("error") > -1) {
        prefix = "../";
    }
    location.href= prefix + "passwordRecoveryToken/" + $j("#j_username").val();
}
