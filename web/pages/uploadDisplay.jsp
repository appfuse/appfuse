<%@ include file="/common/taglibs.jsp"%>

Below is a list of attributes that were gathered in UploadAction.java.

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
    	<td>
            <c:out value="${location}" escapeXml="false"/>
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <input type="button" name="done" id="done" value="Done"
                onclick="location.href='<html:rewrite forward="mainMenu"/>'" />
            <input type="button" name="done" id="done" value="Upload Another"
                onclick="location.href='<html:rewrite forward="selectFile"/>'" />
        </td>
    </tr>
</table>


