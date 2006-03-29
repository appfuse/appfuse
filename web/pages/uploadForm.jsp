<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.title"/></title>
<content tag="heading"><fmt:message key="upload.heading"/></content>

<!--
	The most important part is to declare your form's enctype to be "multipart/form-data",
	and to have a form:file element that maps to your ActionForm's FormFile property
-->

<p><fmt:message key="upload.message"/></p>
<div class="separator"></div>

<html:form action="uploadFile" method="post" styleId="uploadForm"
    enctype="multipart/form-data" onsubmit="return validateUploadForm(this)">
<table class="detail">
    <tr>
        <th>
            <appfuse:label key="uploadForm.name"/>
        </th>
        <td>
            <html:text property="name" size="40" styleId="name" />
        </td>
    </tr>
    <tr>
        <th>
            <appfuse:label key="uploadForm.file"/>
        </th>
        <td>
            <html:file property="file" size="50" styleId="file" />
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <html:submit styleClass="button" onclick="bCancel=false">
            	<fmt:message key="button.upload"/>
            </html:submit>
            <html:cancel styleClass="button" onclick="bCancel=true">
            	<fmt:message key="button.cancel"/>
            </html:cancel>
        </td>
    </tr>
</table>

</html:form>

<script type="text/javascript">
    Form.focusFirstElement(document.forms['uploadForm']);
    highlightFormElements();
</script>
<html:javascript formName="uploadForm" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>