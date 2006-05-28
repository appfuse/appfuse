<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.title"/></title>
<content tag="heading"><fmt:message key="upload.heading"/></content>
<meta name="menu" content="FileUpload"/>

<ww:form action="uploadFile" enctype="multipart/form-data" method="post"
    validate="true" name="upload">
    <li class="info">
        <fmt:message key="upload.message"/>
    </li>
    <ww:textfield label="%{getText('uploadForm.name')}" name="name"
        cssClass="text medium" value="name" size="40" required="true"/>
    <ww:file label="%{getText('uploadForm.file')}" name="file" id="file" 
        cssClass="text file" required="true"/>
    <li class="buttonBar bottom">
        <input type="submit" name="upload" class="button" onclick="bCancel=false"
            value="<fmt:message key="button.upload"/>" />
        <input type="button" name="cancel" class="button" onclick="location.href='mainMenu.html'"
            value="<fmt:message key="button.cancel"/>" />
    </li>
</ww:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms["upload"]);
</script>

