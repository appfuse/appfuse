<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.title"/></title>
<content tag="heading"><fmt:message key="upload.heading"/></content>

<!--
	The most important part is to declare your form's enctype to be "multipart/form-data"
-->

<fmt:message key="upload.message"/>
<div class="separator"></div>

<form method="post" id="uploadForm" action="<c:url value="/uploadFile.html"/>"
    enctype="multipart/form-data" onsubmit="return validateUploadForm(this)">
<table class="detail">
    <tr>
        <th>
            <appfuse:label key="uploadForm.name" />
        </th>
        <td>
            <input type="text" name="name" id="name" size="40"/>
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="uploadForm.file"/>
        </th>
        <td>
            <input type="file" name="file" id="file" size="50"/>
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <input type="submit" name="upload" class="button" onclick="bCancel=false"
                value="<fmt:message key="button.upload"/>" />
            <input type="submit" name="cancel" class="button" onclick="bCancel=true"
                value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</table>
</form>

<script type="text/javascript">
<!--
highlightFormElements();
// -->
</script>
<html:javascript formName="uploadForm" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>