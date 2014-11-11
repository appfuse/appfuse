define("app/login", ["jquery", "t5/core/console"], function ($, console) {

    function init(spec) {
        alert('init called!');
        // URLs
        var passwordHintUrl = spec.passwordHintUrl;
        var passwordResetUrl = spec.passwordResetUrl;

        // Cookies
        if ($.cookie("username") != null && $.cookie("username") != "") {
            $("#j_username").val($.cookie("username"));
            $("#j_password").focus();
        } else {
            $("#j_username").focus();
        }

        // Event handlers
        $("#passwordHint").click(function (event) {
            event.preventDefault();
            var username = $("#j_username").val();
            location.href = passwordHintUrl + "/" + username;
        });


        $("#passwordReset").click(function (event) {
            event.preventDefault();
            var username = $("#j_username").val();
            location.href = passwordResetUrl + "/" + username;
        });


        $("#loginForm").submit(function (event) {
            //event.preventDefault();
            saveUsername(this);
        });
    }

    function saveUsername(theForm) {
        console.log("Saving username in cookie");
        $.cookie("username", theForm.j_username.value, {expires: 30, path: "/"});
    }

    return {
        init: init,
        saveUsername: saveUsername
    };
});
