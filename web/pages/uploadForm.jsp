<%@ include file="/common/taglibs.jsp"%>

<!--
	The most important part is to declare your form's enctype to be "multipart/form-data",
	and to have a form:file element that maps to your ActionForm's FormFile property
-->

<fmt:message key="upload.message"/>
<div class="separator"></div>

<html:form action="uploadFile" method="post" styleId="uploadForm"
    enctype="multipart/form-data" onsubmit="return validateUploadForm(this)">
<table class="detail">
    <tr>
        <th>
            <label for="name" class="required">
                <bean:message key="uploadForm.name" />*:
            </label>
            <%-- LabelTag is not module-compliant yet 
            <appfuse:label key="uploadForm.name"/>
            --%>
        </th>
        <td>
            <html:text property="name" size="40"
                styleId="name" />
        </td>
    </tr>
    <tr>
        <th>
            <label for="theFile" class="required">
                <bean:message key="uploadForm.file" />*:
            </label>
        </th>
        <td>
            <html:file property="file" size="50"
                styleId="theFile" />
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <html:submit styleClass="button" onclick="bCancel=false">
            	<bean:message key="button.upload"/>
            </html:submit>
            <html:cancel styleClass="button" onclick="bCancel=true">
            	<bean:message key="button.cancel"/>
            </html:cancel>
        </td>
    </tr>
</table>

</html:form>

<script type="text/javascript">
<!--
highlightFormElements();
// -->
</script>
<html:javascript formName="uploadForm" cdata="false"
    dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" 
    src="<c:url value="/scripts/validator.jsp"/>"></script>