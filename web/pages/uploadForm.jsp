<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.title"/></title>
<content tag="heading"><fmt:message key="upload.heading"/></content>
<meta name="menu" content="FileUpload"/>

<!--
    The most important part is to declare your form's enctype to be "multipart/form-data",
    and to have a form:file element that maps to your ActionForm's FormFile property
-->

<html:form action="uploadFile" method="post" styleId="uploadForm"
    enctype="multipart/form-data" onsubmit="return validateUploadForm(this)">
<ul>
    <li class="info">
        <fmt:message key="upload.message"/>
    </li>
    <li>
        <appfuse:label key="uploadForm.name" styleClass="desc"/>
        <html:text property="name" size="40" styleId="name" styleClass="text medium"/>
    </li>
    <li>
        <appfuse:label key="uploadForm.file" styleClass="desc"/>
        <html:file property="file" styleClass="file medium" styleId="file" />
    </li>
    <li class="buttonBar">
        <html:submit styleClass="button" onclick="bCancel=false">
            <fmt:message key="button.upload"/>
        </html:submit>
        <html:cancel styleClass="button" onclick="bCancel=true">
            <fmt:message key="button.cancel"/>
        </html:cancel>
    </li>
</ul>
</html:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms['uploadForm']);
    highlightFormElements();
</script>
<html:javascript formName="uploadForm" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>