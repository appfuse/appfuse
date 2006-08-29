function clearErrorMessages(form) {
	// clear out any rows with an "errorFor" attribute
	var divs = form.getElementsByTagName("div");
    var paragraphsToDelete = new Array();

    for(var i = 0; i < divs.length; i++) {
        var p = divs[i];
        if (p.getAttribute("errorFor")) {
            paragraphsToDelete.push(p);
        }
    }

    // now delete the paragraphsToDelete
    for (var i = 0; i < paragraphsToDelete.length; i++) {
        var r = paragraphsToDelete[i];
        var parent = r.parentNode;
        parent.removeChild(r);
    }
}

function clearErrorLabels(form) {
    // set all labels back to the normal class
    var labels = form.getElementsByTagName("label");
    for (var i = 0; i < labels.length; i++) {
        var label = labels[i];
        if (label) {
            if (label.className.indexOf("error") > -1) {
                label.className = label.className.substring(0, label.className.indexOf("error"));
            }
        }
    }

}

function addError(e, errorText) {
    try {
        var ctrlDiv = e.parentNode; // wwctrl_ div or span
        var enclosingDiv = ctrlDiv.parentNode; // wwgrp_ div

        /*alert(ctrlDiv.nodeName);
		if (!ctrlDiv || (ctrlDiv.nodeName != "DIV" && ctrlDiv.nodeName != "SPAN") || !enclosingDiv || enclosingDiv.nodeName != "DIV") {
			alert("do not validate:" + e.id);
			return;
		}*/
		
        var label = enclosingDiv.getElementsByTagName("label")[0];
		if (label) {
		    label.className += " error";
	    }
	    
	    var input = enclosingDiv.getElementsByTagName("input")[0];
	    if (input) {
	        input.className += " error";
	    }

		var firstDiv = enclosingDiv.getElementsByTagName("div")[0]; // either wwctrl_ or wwlbl_
		if (!firstDiv) {
			firstDiv = enclosingDiv.getElementsByTagName("span")[0];
		}
        var error = document.createTextNode(errorText);
        var errorDiv = document.createElement("div");
        
        errorDiv.className = "errorMessage";
        errorDiv.setAttribute("errorFor", e.id);;
        errorDiv.appendChild(error);
        firstDiv.appendChild(errorDiv);
    } catch (e) {
        alert(e);
    }
}


