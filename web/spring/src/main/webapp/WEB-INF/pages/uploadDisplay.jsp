<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="display.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<h2><fmt:message key="display.heading"/></h2>
<p>Below is a list of attributes that were gathered in FileUploadController.java.</p>

<table class="table-striped" cellpadding="5">
    <tr>
        <th>Friendly Name:</th>
        <td><c:out value="${friendlyName}"/></td>
    </tr>
    <tr>
        <th>Filename:</th>
        <td><c:out value="${fileName}"/></td>
    </tr>
    <tr>
        <th>File content type:</th>
        <td><c:out value="${contentType}"/></td>
    </tr>
    <tr>
        <th>File size:</th>
        <td><c:out value="${size}"/></td>
    </tr>
    <tr>
        <th class="tallCell">File Location:</th>
        <td>The file has been written to: <br />
            <a href="<c:out value="${link}"/>"><c:out value="${location}" escapeXml="false"/></a>
        </td>
    </tr>
</table>
<fieldset class="form-actions">
    <input type="btn btn-primary" name="done" id="done" value="Done"
        onclick="location.href='mainMenu'" />
    <input type="btn" style="width: 120px" value="Upload Another"
        onclick="location.href='fileupload'" />
</fieldset>


