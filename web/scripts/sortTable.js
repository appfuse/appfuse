// This function is used to setup sorting on a table's headers
function initTable(id, colTypes) {
    var table = document.getElementById(id);
    // error handling
    if (isUndefined(table)) {
        alert("A table with id=\""+id+"\" does not exist.");
    }
    var thead = table.getElementsByTagName("thead")[0];
    if (isUndefined(thead)) {
        alert("A <thead> doesn't exist on table with id=\""+id+"\".");
    }
    var cols = thead.getElementsByTagName("th");
    // loop through each header and attach an onclick event
    for (i=0; i < cols.length; i++) {
        if (!isUndefined(colTypes) && colTypes[i] != null) {
            cols[i].onclick = function() {sortTable(this,colTypes[i])};
        } else {
            cols[i].onclick = function() {sortTable(this)};
        }
        cols[i].className="sortable";
        // set sort indicator on first column
        if (i==0) {
           setIndicator(cols[i], true);
        }
    }
}

var sortedOn = 0;
var lastSorted = "";

function sortTable(e, type) {

    // figure out which column this is in the table, and the table
    table = e.parentNode.parentNode.parentNode;
    sortOn = e.cellIndex;
    
    //var table = document.getElementById(tableId);
    var tbody = table.getElementsByTagName('tbody')[0];
    var rows = tbody.getElementsByTagName('tr');

    var rowArray = new Array();
    for (var i=0, length=rows.length; i<length; i++) {
        rowArray[i] = rows[i].cloneNode(true);
    }
    
    if (sortOn == sortedOn) { 
        rowArray.reverse(); 
        lastSorted = (lastSorted == "asc" || lastSorted == "") ? "desc" : "asc";
    } else {
        lastSorted = "asc";
        sortedOn = sortOn;
        try {
            if (type == "number") {
                rowArray.sort(rowCompareNumbers);
            } else if (type == "input") {
                rowArray.sort(rowCompareInputs);
            } else if (type == "numberInput") {
                rowArray.sort(rowCompareNumberInputs);
            } else if (type == "select") {
                rowArray.sort(rowCompareSelects);
            } else if (type == "dollar") {
                rowArray.sort(rowCompareDollars);
            } else {
                rowArray.sort(rowCompare);
            } 
        } catch (err) {
            alert("An error has occurred!\nName: " + err.name + "\nMessage: " + err.message);
        }
    }
          
    var newTbody = document.createElement('tbody');
    for (var i=0, length=rowArray.length; i<length; i++) {
        // set the appropriate className for alternating rows
        if (i % 2 == 0) {
            rowArray[i].className = "tableRowOdd";
        } else {
            rowArray[i].className = "tableRowEven";
        }
        newTbody.appendChild(rowArray[i]);
    }
    
    table.replaceChild(newTbody, tbody);
    
    // get all the cells in the thead and set the class on the sorted column
    var thead = table.getElementsByTagName('thead')[0];
    var cells = thead.getElementsByTagName('th');
    for (var i=0; i < cells.length; i++) {
        cells[i].className = "sortable";    
        var spans = cells[i].getElementsByTagName('img');
        for (var j=0; j < spans.length; j++) {
            cells[i].removeChild(spans[j]);
        }
    }
    setIndicator(e);
}

function rowCompare(a, b) {
    var aVal = a.getElementsByTagName('td')[sortedOn].firstChild.nodeValue;
    var bVal = b.getElementsByTagName('td')[sortedOn].firstChild.nodeValue;
    return (aVal == bVal ? 0 : (aVal > bVal ? 1 : -1));
}

function rowCompareInputs(a, b) {
    var aVal = a.getElementsByTagName('td')[sortedOn].getElementsByTagName("input")[0].value;
    var bVal = b.getElementsByTagName('td')[sortedOn].getElementsByTagName("input")[0].value;
    return (aVal == bVal ? 0 : (aVal > bVal ? 1 : -1));
}

function rowCompareNumbers(a, b) {
    var aVal = parseInt(a.getElementsByTagName('td')[sortedOn].firstChild.nodeValue);
    var bVal = parseInt(b.getElementsByTagName('td')[sortedOn].firstChild.nodeValue);
    return (aVal - bVal);
}

function rowCompareNumberInputs(a, b) {
    var aVal = parseInt(a.getElementsByTagName('td')[sortedOn].getElementsByTagName("input")[0].value);
    var bVal = parseInt(b.getElementsByTagName('td')[sortedOn].getElementsByTagName("input")[0].value);
    return (aVal - bVal);
}

function rowCompareSelects(a, b) {
    var aSelect = a.getElementsByTagName('td')[sortedOn].getElementsByTagName("select")[0];
    var bSelect = b.getElementsByTagName('td')[sortedOn].getElementsByTagName("select")[0];
    var aVal = aSelect.options[aSelect.selectedIndex].text;
    var bVal = bSelect.options[bSelect.selectedIndex].text;
    return (aVal == bVal ? 0 : (aVal > bVal ? 1 : -1));
}

function rowCompareDollars(a, b) {
    var aVal = parseFloat(a.getElementsByTagName('td')[sortedOn].firstChild.nodeValue.substr(1));
    var bVal = parseFloat(b.getElementsByTagName('td')[sortedOn].firstChild.nodeValue.substr(1));
    return (aVal - bVal);
}

function setIndicator(e, pageLoad) {
    if (pageLoad) {
        lastSorted = "asc";
    }
    e.className = "sorted";
    var indicator = document.createElement("img");
    indicator.setAttribute("src", "images/"+lastSorted+".gif");
    indicator.setAttribute("width", "9");
    indicator.setAttribute("height", "10");
    indicator.setAttribute("alt", (lastSorted == "asc") ? "Ascending" : "Descending");
    indicator.className = "sortIndicator";
    e.appendChild(indicator);
}