
// This function is used by the login screen to validate user/pass
// are entered.
function validateRequired(form) {
    var bValid = true;
    var focusField = null;
    var i = 0;
    var fields = new Array();
    oRequired = new required();

    for (x in oRequired) {
        if ((form[oRequired[x][0]].type == 'text' || form[oRequired[x][0]].type == 'textarea' || form[oRequired[x][0]].type == 'select-one' || form[oRequired[x][0]].type == 'radio' || form[oRequired[x][0]].type == 'password') && form[oRequired[x][0]].value == '') {
           if (i == 0)
              focusField = form[oRequired[x][0]];

           fields[i++] = oRequired[x][1];

           bValid = false;
        }
    }

    if (fields.length > 0) {
       focusField.focus();
       alert(fields.join('\n'));
    }

    return bValid;
}

// This function is a generic function to create form elements
function createFormElement(element, type, name, id, value, parent) {
    var e = document.createElement(element);
    e.setAttribute("name", name);
    e.setAttribute("type", type);
    e.setAttribute("id", id);
    e.setAttribute("value", value);
    parent.appendChild(e);
}

function confirmDelete(obj) {
    var msg = "Are you sure you want to delete this " + obj + "?";
    ans = confirm(msg);
    return ans;
}

// 18n version of confirmDelete. Message must be already built.
function confirmMessage(obj) {
    var msg = "" + obj;
    ans = confirm(msg);
    return ans;
}
