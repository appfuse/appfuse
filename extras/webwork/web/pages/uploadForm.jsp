<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.title"/></title>
<content tag="heading"><fmt:message key="upload.heading"/></content>

<fmt:message key="upload.message"/>
<div class="separator"></div>

<ww:form action="uploadFile" enctype="multipart/form-data" method="post"
    validate="true" name="upload">
    <ww:textfield label="%{getText('uploadForm.name')}" name="name"
        value="name" size="40" required="true"/>
    <ww:file label="%{getText('uploadForm.file')}" name="file" id="file" 
        size="50" required="true"/>
    <tr>
        <td></td>
        <td class="buttonBar">
            <input type="submit" name="upload" class="button" onclick="bCancel=false"
                value="<fmt:message key="button.upload"/>" />
            <input type="button" name="cancel" class="button" onclick="location.href='mainMenu.html'"
                value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</ww:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms["upload"]);
</script>

