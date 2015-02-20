package org.appfuse.webapp.client.application;

/**
 * Interface to represent the messages contained in resource bundle:
 * /home/opt/workspaces
 * /AppFuse/appfuse.git/web/gwt/target/classes/org/appfuse/webapp
 * /client/application/ApplicationResources.properties'.
 */
public interface ApplicationResources extends com.google.gwt.i18n.client.Messages {

    /**
     * Translated
     * "Your current role does not allow you to view this page.  Please contact your system administrator if you believe you should have access.  In the meantime, how about a pretty picture to cheer you up?"
     * .
     *
     * @return translated
     *         "Your current role does not allow you to view this page.  Please contact your system administrator if you believe you should have access.  In the meantime, how about a pretty picture to cheer you up?"
     */
    @DefaultMessage("Your current role does not allow you to view this page.  Please contact your system administrator if you believe you should have access.  In the meantime, how about a pretty picture to cheer you up?")
    @Key("403.message")
    String _03_message();

    /**
     * Translated "Access Denied".
     *
     * @return translated "Access Denied"
     */
    @DefaultMessage("Access Denied")
    @Key("403.title")
    String _03_title();

    /**
     * Translated
     * "The page you requested was not found.  You might try returning to the <a href=\"{0}\">Home</a>. While you&#39;re here, how about a pretty picture to cheer you up?"
     * .
     *
     * @return translated
     *         "The page you requested was not found.  You might try returning to the <a href=\"{0}\">Home</a>. While you&#39;re here, how about a pretty picture to cheer you up?"
     */
    @DefaultMessage("The page you requested was not found.  You might try returning to the <a href=\"{0}\">Home</a>. While you&#39;re here, how about a pretty picture to cheer you up?")
    @Key("404.message")
    String _04_message(String arg0);

    /**
     * Translated "Page Not Found".
     *
     * @return translated "Page Not Found"
     */
    @DefaultMessage("Page Not Found")
    @Key("404.title")
    String _04_title();

    /**
     * Translated "Full Name".
     *
     * @return translated "Full Name"
     */
    @DefaultMessage("Full Name")
    @Key("activeUsers.fullName")
    String activeUsers_fullName();

    /**
     * Translated "Current Users".
     *
     * @return translated "Current Users"
     */
    @DefaultMessage("Current Users")
    @Key("activeUsers.heading")
    String activeUsers_heading();

    /**
     * Translated
     * "The following is a list of users that have logged in and their sessions have not expired."
     * .
     *
     * @return translated
     *         "The following is a list of users that have logged in and their sessions have not expired."
     */
    @DefaultMessage("The following is a list of users that have logged in and their sessions have not expired.")
    @Key("activeUsers.message")
    String activeUsers_message();

    /**
     * Translated
     * "{0} User(s) found, displaying {1} user(s), from {2} to {3}. Page {4} / {5}"
     * .
     *
     * @return translated
     *         "{0} User(s) found, displaying {1} user(s), from {2} to {3}. Page {4} / {5}"
     */
    @DefaultMessage("{0} User(s) found, displaying {1} user(s), from {2} to {3}. Page {4} / {5}")
    @Key("activeUsers.summary")
    String activeUsers_summary(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5);

    /**
     * Translated "Active Users".
     *
     * @return translated "Active Users"
     */
    @DefaultMessage("Active Users")
    @Key("activeUsers.title")
    String activeUsers_title();

    /**
     * Translated "Add".
     *
     * @return translated "Add"
     */
    @DefaultMessage("Add")
    @Key("button.add")
    String button_add();

    /**
     * Translated "Cancel".
     *
     * @return translated "Cancel"
     */
    @DefaultMessage("Cancel")
    @Key("button.cancel")
    String button_cancel();

    /**
     * Translated "Copy".
     *
     * @return translated "Copy"
     */
    @DefaultMessage("Copy")
    @Key("button.copy")
    String button_copy();

    /**
     * Translated "Delete".
     *
     * @return translated "Delete"
     */
    @DefaultMessage("Delete")
    @Key("button.delete")
    String button_delete();

    /**
     * Translated "Done".
     *
     * @return translated "Done"
     */
    @DefaultMessage("Done")
    @Key("button.done")
    String button_done();

    /**
     * Translated "Edit".
     *
     * @return translated "Edit"
     */
    @DefaultMessage("Edit")
    @Key("button.edit")
    String button_edit();

    /**
     * Translated "Login".
     *
     * @return translated "Login"
     */
    @DefaultMessage("Login")
    @Key("button.login")
    String button_login();

    /**
     * Translated "Signup".
     *
     * @return translated "Signup"
     */
    @DefaultMessage("Signup")
    @Key("button.register")
    String button_register();

    /**
     * Translated "Reset".
     *
     * @return translated "Reset"
     */
    @DefaultMessage("Reset")
    @Key("button.reset")
    String button_reset();

    /**
     * Translated "Save".
     *
     * @return translated "Save"
     */
    @DefaultMessage("Save")
    @Key("button.save")
    String button_save();

    /**
     * Translated "Search".
     *
     * @return translated "Search"
     */
    @DefaultMessage("Search")
    @Key("button.search")
    String button_search();

    /**
     * Translated "Upload".
     *
     * @return translated "Upload"
     */
    @DefaultMessage("Upload")
    @Key("button.upload")
    String button_upload();

    /**
     * Translated "View".
     *
     * @return translated "View"
     */
    @DefaultMessage("View")
    @Key("button.view")
    String button_view();

    /**
     * Translated "mm/dd/yyyy".
     *
     * @return translated "mm/dd/yyyy"
     */
    @DefaultMessage("mm/dd/yyyy")
    @Key("calendar.format")
    String calendar_format();

    /**
     * Translated "0".
     *
     * @return translated "0"
     */
    @DefaultMessage("0")
    @Key("calendar.weekstart")
    String calendar_weekstart();

    /**
     * Translated "Your Company Here".
     *
     * @return translated "Your Company Here"
     */
    @DefaultMessage("Your Company Here")
    @Key("company.name")
    String company_name();

    /**
     * Translated "http://raibledesigns.com".
     *
     * @return translated "http://raibledesigns.com"
     */
    @DefaultMessage("http://raibledesigns.com")
    @Key("company.url")
    String company_url();

    /**
     * Translated "2003-2014".
     *
     * @return translated "2003-2014"
     */
    @DefaultMessage("2003-2014")
    @Key("copyright.year")
    String copyright_year();

    /**
     * Translated "MM/dd/yyyy".
     *
     * @return translated "MM/dd/yyyy"
     */
    @DefaultMessage("MM/dd/yyyy")
    @Key("date.format")
    String date_format();

    /**
     * Translated "Are you sure you want to delete this {0}?".
     *
     * @return translated "Are you sure you want to delete this {0}?"
     */
    @DefaultMessage("Are you sure you want to delete this {0}?")
    @Key("delete.confirm")
    String delete_confirm(String arg0);

    /**
     * Translated "File Information".
     *
     * @return translated "File Information"
     */
    @DefaultMessage("File Information")
    @Key("display.heading")
    String display_heading();

    /**
     * Translated "File Uploaded Successfully!".
     *
     * @return translated "File Uploaded Successfully!"
     */
    @DefaultMessage("File Uploaded Successfully!")
    @Key("display.title")
    String display_title();

    /**
     * Translated "Form information was deleted successfully.".
     *
     * @return translated "Form information was deleted successfully."
     */
    @DefaultMessage("Form information was deleted successfully.")
    @Key("entity.deleted")
    String entity_deleted();

    /**
     * Translated "Form information was saved successfully.".
     *
     * @return translated "Form information was saved successfully."
     */
    @DefaultMessage("Form information was saved successfully.")
    @Key("entity.saved")
    String entity_saved();

    /**
     * Translated "Yikes!".
     *
     * @return translated "Yikes!"
     */
    @DefaultMessage("Yikes!")
    @Key("errorPage.heading")
    String errorPage_heading();

    /**
     * Translated "Please check your log files for further information.".
     *
     * @return translated "Please check your log files for further information."
     */
    @DefaultMessage("Please check your log files for further information.")
    @Key("errorPage.message")
    String errorPage_message();

    /**
     * Translated "An error has occurred".
     *
     * @return translated "An error has occurred"
     */
    @DefaultMessage("An error has occurred")
    @Key("errorPage.title")
    String errorPage_title();

    /**
     * Translated "{0} must be an byte.".
     *
     * @return translated "{0} must be an byte."
     */
    @DefaultMessage("{0} must be an byte.")
    @Key("errors.byte")
    String errors_byte(String arg0);

    /**
     * Translated "Operation cancelled.".
     *
     * @return translated "Operation cancelled."
     */
    @DefaultMessage("Operation cancelled.")
    @Key("errors.cancel")
    String errors_cancel();

    /**
     * Translated
     * "An error occurred while converting web values to data values.".
     *
     * @return translated
     *         "An error occurred while converting web values to data values."
     */
    @DefaultMessage("An error occurred while converting web values to data values.")
    @Key("errors.conversion")
    String errors_conversion();

    /**
     * Translated "{0} is not a valid credit card number.".
     *
     * @return translated "{0} is not a valid credit card number."
     */
    @DefaultMessage("{0} is not a valid credit card number.")
    @Key("errors.creditcard")
    String errors_creditcard(String arg0);

    /**
     * Translated "{0} is not a date.".
     *
     * @return translated "{0} is not a date."
     */
    @DefaultMessage("{0} is not a date.")
    @Key("errors.date")
    String errors_date(String arg0);

    /**
     * Translated "{0}".
     *
     * @return translated "{0}"
     */
    @DefaultMessage("{0}")
    @Key("errors.detail")
    String errors_detail(String arg0);

    /**
     * Translated "{0} must be an double.".
     *
     * @return translated "{0} must be an double."
     */
    @DefaultMessage("{0} must be an double.")
    @Key("errors.double")
    String errors_double(String arg0);

    /**
     * Translated "{0} is an invalid e-mail address.".
     *
     * @return translated "{0} is an invalid e-mail address."
     */
    @DefaultMessage("{0} is an invalid e-mail address.")
    @Key("errors.email")
    String errors_email(String arg0);

    /**
     * Translated
     * "This username ({0}) or e-mail address ({1}) already exists.  Please try a different username."
     * .
     *
     * @return translated
     *         "This username ({0}) or e-mail address ({1}) already exists.  Please try a different username."
     */
    @DefaultMessage("This username ({0}) or e-mail address ({1}) already exists.  Please try a different username.")
    @Key("errors.existing.user")
    String errors_existing_user(String arg0, String arg1);

    /**
     * Translated "{0} must be an float.".
     *
     * @return translated "{0} must be an float."
     */
    @DefaultMessage("{0} must be an float.")
    @Key("errors.float")
    String errors_float(String arg0);

    /**
     * Translated "The process did not complete. Details should follow.".
     *
     * @return translated "The process did not complete. Details should follow."
     */
    @DefaultMessage("The process did not complete. Details should follow.")
    @Key("errors.general")
    String errors_general();

    /**
     * Translated "{0} must be a number.".
     *
     * @return translated "{0} must be a number."
     */
    @DefaultMessage("{0} must be a number.")
    @Key("errors.integer")
    String errors_integer(String arg0);

    /**
     * Translated "{0} is invalid.".
     *
     * @return translated "{0} is invalid."
     */
    @DefaultMessage("{0} is invalid.")
    @Key("errors.invalid")
    String errors_invalid(String arg0);

    /**
     * Translated "{0} must be an long.".
     *
     * @return translated "{0} must be an long."
     */
    @DefaultMessage("{0} must be an long.")
    @Key("errors.long")
    String errors_long(String arg0);

    /**
     * Translated "{0} can not be greater than {1} characters.".
     *
     * @return translated "{0} can not be greater than {1} characters."
     */
    @DefaultMessage("{0} can not be greater than {1} characters.")
    @Key("errors.maxlength")
    String errors_maxlength(String arg0, String arg1);

    /**
     * Translated "{0} can not be less than {1} characters.".
     *
     * @return translated "{0} can not be less than {1} characters."
     */
    @DefaultMessage("{0} can not be less than {1} characters.")
    @Key("errors.minlength")
    String errors_minlength(String arg0, String arg1);

    /**
     * Translated "No error message was found, check your server logs.".
     *
     * @return translated "No error message was found, check your server logs."
     */
    @DefaultMessage("No error message was found, check your server logs.")
    @Key("errors.none")
    String errors_none();

    /**
     * Translated "Invalid username and/or password, please try again.".
     *
     * @return translated "Invalid username and/or password, please try again."
     */
    @DefaultMessage("Invalid username and/or password, please try again.")
    @Key("errors.password.mismatch")
    String errors_password_mismatch();

    /**
     * Translated "{0} is an invalid phone number.".
     *
     * @return translated "{0} is an invalid phone number."
     */
    @DefaultMessage("{0} is an invalid phone number.")
    @Key("errors.phone")
    String errors_phone(String arg0);

    /**
     * Translated "{0} is not in the range {1} through {2}.".
     *
     * @return translated "{0} is not in the range {1} through {2}."
     */
    @DefaultMessage("{0} is not in the range {1} through {2}.")
    @Key("errors.range")
    String errors_range(String arg0, String arg1, String arg2);

    /**
     * Translated "{0} is a required field.".
     *
     * @return translated "{0} is a required field."
     */
    @DefaultMessage("{0} is a required field.")
    @Key("errors.required")
    String errors_required(String arg0);

    /**
     * Translated "An error has occurred while sending an email".
     *
     * @return translated "An error has occurred while sending an email"
     */
    @DefaultMessage("An error has occurred while sending an email")
    @Key("errors.sending.email")
    String errors_sending_email();

    /**
     * Translated "{0} must be an short.".
     *
     * @return translated "{0} must be an short."
     */
    @DefaultMessage("{0} must be an short.")
    @Key("errors.short")
    String errors_short(String arg0);

    /**
     * Translated
     * "Request could not be completed. Operation is not in sequence.".
     *
     * @return translated
     *         "Request could not be completed. Operation is not in sequence."
     */
    @DefaultMessage("Request could not be completed. Operation is not in sequence.")
    @Key("errors.token")
    String errors_token();

    /**
     * Translated "The {0} field has to have the same value as the {1} field.".
     *
     * @return translated
     *         "The {0} field has to have the same value as the {1} field."
     */
    @DefaultMessage("The {0} field has to have the same value as the {1} field.")
    @Key("errors.twofields")
    String errors_twofields(String arg0, String arg1);

    /**
     * Translated "{0} is an invalid zip code.".
     *
     * @return translated "{0} is an invalid zip code."
     */
    @DefaultMessage("{0} is an invalid zip code.")
    @Key("errors.zip")
    String errors_zip(String arg0);

    /**
     * Translated "Current Users".
     *
     * @return translated "Current Users"
     */
    @DefaultMessage("Current Users")
    @Key("home.activeUsers")
    String home_activeUsers();

    /**
     * Translated "Welcome!".
     *
     * @return translated "Welcome!"
     */
    @DefaultMessage("Welcome!")
    @Key("home.heading")
    String home_heading();

    /**
     * Translated
     * "Congratulations, you have logged in successfully!  Now that you''ve logged in, you have the following options:"
     * .
     *
     * @return translated
     *         "Congratulations, you have logged in successfully!  Now that you''ve logged in, you have the following options:"
     */
    @DefaultMessage("Congratulations, you have logged in successfully!  Now that you''ve logged in, you have the following options:")
    @Key("home.message")
    String home_message();

    /**
     * Translated "Home".
     *
     * @return translated "Home"
     */
    @DefaultMessage("Home")
    @Key("home.title")
    String home_title();

    /**
     * Translated "E-Mail".
     *
     * @return translated "E-Mail"
     */
    @DefaultMessage("E-Mail")
    @Key("icon.email")
    String icon_email();

    /**
     * Translated "/images/iconEmail.gif".
     *
     * @return translated "/images/iconEmail.gif"
     */
    @DefaultMessage("/images/iconEmail.gif")
    @Key("icon.email.img")
    String icon_email_img();

    /**
     * Translated "Information".
     *
     * @return translated "Information"
     */
    @DefaultMessage("Information")
    @Key("icon.information")
    String icon_information();

    /**
     * Translated "/images/iconInformation.gif".
     *
     * @return translated "/images/iconInformation.gif"
     */
    @DefaultMessage("/images/iconInformation.gif")
    @Key("icon.information.img")
    String icon_information_img();

    /**
     * Translated "Warning".
     *
     * @return translated "Warning"
     */
    @DefaultMessage("Warning")
    @Key("icon.warning")
    String icon_warning();

    /**
     * Translated "/images/iconWarning.gif".
     *
     * @return translated "/images/iconWarning.gif"
     */
    @DefaultMessage("/images/iconWarning.gif")
    @Key("icon.warning.img")
    String icon_warning_img();

    /**
     * Translated "Password".
     *
     * @return translated "Password"
     */
    @DefaultMessage("Password")
    @Key("label.password")
    String label_password();

    /**
     * Translated "Username".
     *
     * @return translated "Username"
     */
    @DefaultMessage("Username")
    @Key("label.username")
    String label_username();

    /**
     * Translated "Sign In".
     *
     * @return translated "Sign In"
     */
    @DefaultMessage("Sign In")
    @Key("login.heading")
    String login_heading();

    /**
     * Translated
     * "Forgot your password?  Have your <a href=\"?\" onmouseover=\"window.status=''Have your password hint sent to you.''; return true\" onmouseout=\"window.status=''''; return true\" title=\"Have your password hint sent to you.\" onclick=\"passwordHint(); return false\" id=\"passwordHint\">password hint e-mailed to you</a>."
     * .
     *
     * @return translated
     *         "Forgot your password?  Have your <a href=\"?\" onmouseover=\"window.status=''Have your password hint sent to you.''; return true\" onmouseout=\"window.status=''''; return true\" title=\"Have your password hint sent to you.\" onclick=\"passwordHint(); return false\" id=\"passwordHint\">password hint e-mailed to you</a>."
     */
    @DefaultMessage("Forgot your password?  Have your <a href=\"?\" onmouseover=\"window.status=''Have your password hint sent to you.''; return true\" onmouseout=\"window.status=''''; return true\" title=\"Have your password hint sent to you.\" onclick=\"passwordHint(); return false\" id=\"passwordHint\">password hint e-mailed to you</a>.")
    @Key("login.passwordHint")
    String login_passwordHint();

    /**
     * Translated "The username {0} was not found in our database.".
     *
     * @return translated "The username {0} was not found in our database."
     */
    @DefaultMessage("The username {0} was not found in our database.")
    @Key("login.passwordHint.error")
    String login_passwordHint_error(String arg0);

    /**
     * Translated "The password hint for {0} has been sent to {1}.".
     *
     * @return translated "The password hint for {0} has been sent to {1}."
     */
    @DefaultMessage("The password hint for {0} has been sent to {1}.")
    @Key("login.passwordHint.sent")
    String login_passwordHint_sent(String arg0, String arg1);

    /**
     * Translated "Remember Me".
     *
     * @return translated "Remember Me"
     */
    @DefaultMessage("Remember Me")
    @Key("login.rememberMe")
    String login_rememberMe();

    /**
     * Translated "Not a member? <a href=\"{0}\">Signup</a> for an account.".
     *
     * @return translated
     *         "Not a member? <a href=\"{0}\">Signup</a> for an account."
     */
    @DefaultMessage("Not a member? <a href=\"{0}\">Signup</a> for an account.")
    @Key("login.signup")
    String login_signup(String arg0);

    /**
     * Translated "Login".
     *
     * @return translated "Login"
     */
    @DefaultMessage("Login")
    @Key("login.title")
    String login_title();

    /**
     * Translated
     * "The file you are trying to upload is too big.  The maximum allowed size is 2 MB."
     * .
     *
     * @return translated
     *         "The file you are trying to upload is too big.  The maximum allowed size is 2 MB."
     */
    @DefaultMessage("The file you are trying to upload is too big.  The maximum allowed size is 2 MB.")
    @Key("maxLengthExceeded")
    String maxLengthExceeded();

    /**
     * Translated "Administration".
     *
     * @return translated "Administration"
     */
    @DefaultMessage("Administration")
    @Key("menu.admin")
    String menu_admin();

    /**
     * Translated "Reload Options".
     *
     * @return translated "Reload Options"
     */
    @DefaultMessage("Reload Options")
    @Key("menu.admin.reload")
    String menu_admin_reload();

    /**
     * Translated "View Users".
     *
     * @return translated "View Users"
     */
    @DefaultMessage("View Users")
    @Key("menu.admin.users")
    String menu_admin_users();

    /**
     * Translated "Upload A File".
     *
     * @return translated "Upload A File"
     */
    @DefaultMessage("Upload A File")
    @Key("menu.selectFile")
    String menu_selectFile();

    /**
     * Translated "Edit Profile".
     *
     * @return translated "Edit Profile"
     */
    @DefaultMessage("Edit Profile")
    @Key("menu.user")
    String menu_user();

    /**
     * Translated
     * "{0} has created an AppFuse account for you.  Your username and password information is below."
     * .
     *
     * @return translated
     *         "{0} has created an AppFuse account for you.  Your username and password information is below."
     */
    @DefaultMessage("{0} has created an AppFuse account for you.  Your username and password information is below.")
    @Key("newuser.email.message")
    String newuser_email_message(String arg0);

    /**
     * Translated "Reloading options completed successfully.".
     *
     * @return translated "Reloading options completed successfully."
     */
    @DefaultMessage("Reloading options completed successfully.")
    @Key("reload.succeeded")
    String reload_succeeded();

    /**
     * Translated "Name".
     *
     * @return translated "Name"
     */
    @DefaultMessage("Name")
    @Key("roleForm.name")
    String roleForm_name();

    /**
     * Translated "Enter search terms...".
     *
     * @return translated "Enter search terms..."
     */
    @DefaultMessage("Enter search terms...")
    @Key("search.enterTerms")
    String search_enterTerms();

    /**
     * Translated
     * "You have successfully registered for access to AppFuse.  Your username and password information is below."
     * .
     *
     * @return translated
     *         "You have successfully registered for access to AppFuse.  Your username and password information is below."
     */
    @DefaultMessage("You have successfully registered for access to AppFuse.  Your username and password information is below.")
    @Key("signup.email.message")
    String signup_email_message();

    /**
     * Translated "AppFuse Account Information".
     *
     * @return translated "AppFuse Account Information"
     */
    @DefaultMessage("AppFuse Account Information")
    @Key("signup.email.subject")
    String signup_email_subject();

    /**
     * Translated "New User Registration".
     *
     * @return translated "New User Registration"
     */
    @DefaultMessage("New User Registration")
    @Key("signup.heading")
    String signup_heading();

    /**
     * Translated "Please enter your personal information.".
     *
     * @return translated "Please enter your personal information."
     */
    @DefaultMessage("Please enter your personal information.")
    @Key("signup.message")
    String signup_message();

    /**
     * Translated "Sign Up".
     *
     * @return translated "Sign Up"
     */
    @DefaultMessage("Sign Up")
    @Key("signup.title")
    String signup_title();

    /**
     * Translated "Please input your new password.".
     *
     * @return translated "Please input your new password."
     */
    @DefaultMessage("Please input your new password.")
    @Key("updatePassword.changePassword.message")
    String updatePassword_changePassword_message();

    /**
     * Translated "Change Password".
     *
     * @return translated "Change Password"
     */
    @DefaultMessage("Change Password")
    @Key("updatePassword.changePasswordButton")
    String updatePassword_changePasswordButton();

    /**
     * Translated "Change Password".
     *
     * @return translated "Change Password"
     */
    @DefaultMessage("Change Password")
    @Key("updatePassword.changePasswordLink")
    String updatePassword_changePasswordLink();

    /**
     * Translated "Current Password".
     *
     * @return translated "Current Password"
     */
    @DefaultMessage("Current Password")
    @Key("updatePassword.currentPassword.label")
    String updatePassword_currentPassword_label();

    /**
     * Translated "Update your Password".
     *
     * @return translated "Update your Password"
     */
    @DefaultMessage("Update your Password")
    @Key("updatePassword.heading")
    String updatePassword_heading();

    /**
     * Translated "Password provided does not match your current password.".
     *
     * @return translated
     *         "Password provided does not match your current password."
     */
    @DefaultMessage("Password provided does not match your current password.")
    @Key("updatePassword.invalidPassword")
    String updatePassword_invalidPassword();

    /**
     * Translated "Password reset token is not valid or has expired.".
     *
     * @return translated "Password reset token is not valid or has expired."
     */
    @DefaultMessage("Password reset token is not valid or has expired.")
    @Key("updatePassword.invalidToken")
    String updatePassword_invalidToken();

    /**
     * Translated "New Password".
     *
     * @return translated "New Password"
     */
    @DefaultMessage("New Password")
    @Key("updatePassword.newPassword.label")
    String updatePassword_newPassword_label();

    /**
     * Translated
     * "You have requested a password reset. Please input your new password.".
     *
     * @return translated
     *         "You have requested a password reset. Please input your new password."
     */
    @DefaultMessage("You have requested a password reset. Please input your new password.")
    @Key("updatePassword.passwordReset.message")
    String updatePassword_passwordReset_message();

    /**
     * Translated
     * "A password reset link was sent to your registered email address.".
     *
     * @return translated
     *         "A password reset link was sent to your registered email address."
     */
    @DefaultMessage("A password reset link was sent to your registered email address.")
    @Key("updatePassword.recoveryToken.sent")
    String updatePassword_recoveryToken_sent();

    /**
     * Translated
     * "Request a <a href=\"?\" onclick=\"requestRecoveryToken(); return false\">password reset</a> e-mailed to you."
     * .
     *
     * @return translated
     *         "Request a <a href=\"?\" onclick=\"requestRecoveryToken(); return false\">password reset</a> e-mailed to you."
     */
    @DefaultMessage("Request a <a href=\"?\" onclick=\"requestRecoveryToken(); return false\">password reset</a> e-mailed to you.")
    @Key("updatePassword.requestRecoveryTokenLink")
    String updatePassword_requestRecoveryTokenLink();

    /**
     * Translated "Your password has been updated successfully.".
     *
     * @return translated "Your password has been updated successfully."
     */
    @DefaultMessage("Your password has been updated successfully.")
    @Key("updatePassword.success")
    String updatePassword_success();

    /**
     * Translated "Update your Password".
     *
     * @return translated "Update your Password"
     */
    @DefaultMessage("Update your Password")
    @Key("updatePassword.title")
    String updatePassword_title();

    /**
     * Translated "Upload A File".
     *
     * @return translated "Upload A File"
     */
    @DefaultMessage("Upload A File")
    @Key("upload.heading")
    String upload_heading();

    /**
     * Translated
     * "Note that the maximum allowed size of an uploaded file for this application is 2 MB."
     * .
     *
     * @return translated
     *         "Note that the maximum allowed size of an uploaded file for this application is 2 MB."
     */
    @DefaultMessage("Note that the maximum allowed size of an uploaded file for this application is 2 MB.")
    @Key("upload.message")
    String upload_message();

    /**
     * Translated "File Upload".
     *
     * @return translated "File Upload"
     */
    @DefaultMessage("File Upload")
    @Key("upload.title")
    String upload_title();

    /**
     * Translated "File to Upload".
     *
     * @return translated "File to Upload"
     */
    @DefaultMessage("File to Upload")
    @Key("uploadForm.file")
    String uploadForm_file();

    /**
     * Translated "Friendly Name".
     *
     * @return translated "Friendly Name"
     */
    @DefaultMessage("Friendly Name")
    @Key("uploadForm.name")
    String uploadForm_name();

    /**
     * Translated "Expired".
     *
     * @return translated "Expired"
     */
    @DefaultMessage("Expired")
    @Key("user.accountExpired")
    String user_accountExpired();

    /**
     * Translated "Locked".
     *
     * @return translated "Locked"
     */
    @DefaultMessage("Locked")
    @Key("user.accountLocked")
    String user_accountLocked();

    /**
     * Translated "User information for {0} has been added successfully.".
     *
     * @return translated
     *         "User information for {0} has been added successfully."
     */
    @DefaultMessage("User information for {0} has been added successfully.")
    @Key("user.added")
    String user_added(String arg0);

    /**
     * Translated "Address".
     *
     * @return translated "Address"
     */
    @DefaultMessage("Address")
    @Key("user.address.address")
    String user_address_address();

    /**
     * Translated "City".
     *
     * @return translated "City"
     */
    @DefaultMessage("City")
    @Key("user.address.city")
    String user_address_city();

    /**
     * Translated "Country".
     *
     * @return translated "Country"
     */
    @DefaultMessage("Country")
    @Key("user.address.country")
    String user_address_country();

    /**
     * Translated "Zip".
     *
     * @return translated "Zip"
     */
    @DefaultMessage("Zip")
    @Key("user.address.postalCode")
    String user_address_postalCode();

    /**
     * Translated "State".
     *
     * @return translated "State"
     */
    @DefaultMessage("State")
    @Key("user.address.province")
    String user_address_province();

    /**
     * Translated "Available Roles".
     *
     * @return translated "Available Roles"
     */
    @DefaultMessage("Available Roles")
    @Key("user.availableRoles")
    String user_availableRoles();

    /**
     * Translated "Confirm Password".
     *
     * @return translated "Confirm Password"
     */
    @DefaultMessage("Confirm Password")
    @Key("user.confirmPassword")
    String user_confirmPassword();

    /**
     * Translated "Password Expired".
     *
     * @return translated "Password Expired"
     */
    @DefaultMessage("Password Expired")
    @Key("user.credentialsExpired")
    String user_credentialsExpired();

    /**
     * Translated "User Profile for {0} has been deleted successfully.".
     *
     * @return translated "User Profile for {0} has been deleted successfully."
     */
    @DefaultMessage("User Profile for {0} has been deleted successfully.")
    @Key("user.deleted")
    String user_deleted(String arg0);

    /**
     * Translated "E-Mail".
     *
     * @return translated "E-Mail"
     */
    @DefaultMessage("E-Mail")
    @Key("user.email")
    String user_email();

    /**
     * Translated "Enabled".
     *
     * @return translated "Enabled"
     */
    @DefaultMessage("Enabled")
    @Key("user.enabled")
    String user_enabled();

    /**
     * Translated "First Name".
     *
     * @return translated "First Name"
     */
    @DefaultMessage("First Name")
    @Key("user.firstName")
    String user_firstName();

    /**
     * Translated "Id".
     *
     * @return translated "Id"
     */
    @DefaultMessage("Id")
    @Key("user.id")
    String user_id();

    /**
     * Translated "Last Name".
     *
     * @return translated "Last Name"
     */
    @DefaultMessage("Last Name")
    @Key("user.lastName")
    String user_lastName();

    /**
     * Translated "Logout".
     *
     * @return translated "Logout"
     */
    @DefaultMessage("Logout")
    @Key("user.logout")
    String user_logout();

    /**
     * Translated "Password".
     *
     * @return translated "Password"
     */
    @DefaultMessage("Password")
    @Key("user.password")
    String user_password();

    /**
     * Translated "Password Hint".
     *
     * @return translated "Password Hint"
     */
    @DefaultMessage("Password Hint")
    @Key("user.passwordHint")
    String user_passwordHint();

    /**
     * Translated "Phone Number".
     *
     * @return translated "Phone Number"
     */
    @DefaultMessage("Phone Number")
    @Key("user.phoneNumber")
    String user_phoneNumber();

    /**
     * Translated
     * "You have successfully registered for access to this application. ".
     *
     * @return translated
     *         "You have successfully registered for access to this application. "
     */
    @DefaultMessage("You have successfully registered for access to this application. ")
    @Key("user.registered")
    String user_registered();

    /**
     * Translated "Current Roles".
     *
     * @return translated "Current Roles"
     */
    @DefaultMessage("Current Roles")
    @Key("user.roles")
    String user_roles();

    /**
     * Translated "Your profile has been updated successfully.".
     *
     * @return translated "Your profile has been updated successfully."
     */
    @DefaultMessage("Your profile has been updated successfully.")
    @Key("user.saved")
    String user_saved();

    /**
     * Translated "Logged in as: ".
     *
     * @return translated "Logged in as: "
     */
    @DefaultMessage("Logged in as: ")
    @Key("user.status")
    String user_status();

    /**
     * Translated "User information for {0} has been successfully updated.".
     *
     * @return translated
     *         "User information for {0} has been successfully updated."
     */
    @DefaultMessage("User information for {0} has been successfully updated.")
    @Key("user.updated.byAdmin")
    String user_updated_byAdmin(String arg0);

    /**
     * Translated "Username".
     *
     * @return translated "Username"
     */
    @DefaultMessage("Username")
    @Key("user.username")
    String user_username();

    /**
     * Translated "visit".
     *
     * @return translated "visit"
     */
    @DefaultMessage("visit")
    @Key("user.visitWebsite")
    String user_visitWebsite();

    /**
     * Translated "Website".
     *
     * @return translated "Website"
     */
    @DefaultMessage("Website")
    @Key("user.website")
    String user_website();

    /**
     * Translated "Current Users".
     *
     * @return translated "Current Users"
     */
    @DefaultMessage("Current Users")
    @Key("userList.heading")
    String userList_heading();

    /**
     * Translated "<span>No users found.</span>".
     *
     * @return translated "<span>No users found.</span>"
     */
    @DefaultMessage("<span>No users found.</span>")
    @Key("userList.nousers")
    String userList_nousers();

    /**
     * Translated "User List".
     *
     * @return translated "User List"
     */
    @DefaultMessage("User List")
    @Key("userList.title")
    String userList_title();

    /**
     * Translated "user".
     *
     * @return translated "user"
     */
    @DefaultMessage("user")
    @Key("userList.user")
    String userList_user();

    /**
     * Translated "users".
     *
     * @return translated "users"
     */
    @DefaultMessage("users")
    @Key("userList.users")
    String userList_users();

    /**
     * Translated "Account Settings".
     *
     * @return translated "Account Settings"
     */
    @DefaultMessage("Account Settings")
    @Key("userProfile.accountSettings")
    String userProfile_accountSettings();

    /**
     * Translated "User Profile".
     *
     * @return translated "User Profile"
     */
    @DefaultMessage("User Profile")
    @Key("userProfile.admin.heading")
    String userProfile_admin_heading();

    /**
     * Translated "Please update this user''s information.".
     *
     * @return translated "Please update this user''s information."
     */
    @DefaultMessage("Please update this user''s information.")
    @Key("userProfile.admin.message")
    String userProfile_admin_message();

    /**
     * Translated "User Settings".
     *
     * @return translated "User Settings"
     */
    @DefaultMessage("User Settings")
    @Key("userProfile.admin.title")
    String userProfile_admin_title();

    /**
     * Translated "Assign Roles".
     *
     * @return translated "Assign Roles"
     */
    @DefaultMessage("Assign Roles")
    @Key("userProfile.assignRoles")
    String userProfile_assignRoles();

    /**
     * Translated
     * "You cannot change passwords when logging in with the Remember Me feature.  Please logout and log back in to change passwords."
     * .
     *
     * @return translated
     *         "You cannot change passwords when logging in with the Remember Me feature.  Please logout and log back in to change passwords."
     */
    @DefaultMessage("You cannot change passwords when logging in with the Remember Me feature.  Please logout and log back in to change passwords.")
    @Key("userProfile.cookieLogin")
    String userProfile_cookieLogin();

    /**
     * Translated "User Profile".
     *
     * @return translated "User Profile"
     */
    @DefaultMessage("User Profile")
    @Key("userProfile.heading")
    String userProfile_heading();

    /**
     * Translated "Please update your information.".
     *
     * @return translated "Please update your information."
     */
    @DefaultMessage("Please update your information.")
    @Key("userProfile.message")
    String userProfile_message();

    /**
     * Translated "View More Information".
     *
     * @return translated "View More Information"
     */
    @DefaultMessage("View More Information")
    @Key("userProfile.showMore")
    String userProfile_showMore();

    /**
     * Translated "User Settings".
     *
     * @return translated "User Settings"
     */
    @DefaultMessage("User Settings")
    @Key("userProfile.title")
    String userProfile_title();

    /**
     * Translated "AppFuse".
     *
     * @return translated "AppFuse"
     */
    @DefaultMessage("AppFuse")
    @Key("webapp.name")
    String webapp_name();

    /**
     * Translated "Providing integration and style to open source Java.".
     *
     * @return translated "Providing integration and style to open source Java."
     */
    @DefaultMessage("Providing integration and style to open source Java.")
    @Key("webapp.tagline")
    String webapp_tagline();

    /**
     * Translated "Version 3.5.1-SNAPSHOT".
     *
     * @return translated "Version 3.5.1-SNAPSHOT"
     */
    @DefaultMessage("Version 3.5.1-SNAPSHOT")
    @Key("webapp.version")
    String webapp_version();
}
