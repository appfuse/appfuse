<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="display.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<div class="col-sm-10">
    <h2><fmt:message key="display.heading"/></h2>
    <p>Below is a list of attributes that were gathered in UploadAction.java.</p>

    <table class="table-striped" cellpadding="5">
        <tr>
            <th>Friendly Name:</th>
            <td><s:property value="name"/></td>
        </tr>
        <tr>
            <th>Filename:</th>
            <td><s:property value="fileFileName"/></td>
        </tr>
        <tr>
            <th>File content type:</th>
            <td><s:property value="fileContentType"/></td>
        </tr>
        <tr>
            <th>File size:</th>
            <td><s:property value="file.length()"/> bytes</td>
        </tr>
        <tr>
            <th class="tallCell">File Location:</th>
            <td>The file has been written to: <br />
                <a href="<c:out value="${link}"/>">
                <c:out value="${location}" escapeXml="false"/></a>
            </td>
        </tr>
    </table>
    <div id="actions" class="form-group form-actions">
        <a class="btn btn-primary" href="home" >
            <i class="icon-ok icon-white"></i>
            <fmt:message key="button.done"/>
        </a>
        <a class="btn btn-default" href="selectFile" >
            <i class="icon-upload"></i>
            Upload Another
        </a>
    </div>
</div>
