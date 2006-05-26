<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="display.title"/></title>
<content tag="heading"><fmt:message key="display.heading"/></content>
<meta name="menu" content="FileUpload"/>

<p>Below is a list of attributes that were gathered in UploadAction.java.</p>

<div class="separator"></div>

<table class="detail" cellpadding="5">
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
            <a href="<c:out value='${url}'/>"><c:out value="${filePath}"/></a>
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <input type="button" class="button" name="done" value="Done"
                onclick="location.href='<html:rewrite forward="mainMenu"/>'" />
            <input type="button" class="button" style="width: 120px" value="Upload Another"
                onclick="location.href='<html:rewrite forward="selectFile"/>'" />
        </td>
    </tr>
</table>
</form>
