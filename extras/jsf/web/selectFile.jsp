<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.title"/></title>
<content tag="heading"><fmt:message key="upload.heading"/></content>

<!--
    The most important part is to declare your form's enctype to be "multipart/form-data",
    and to have a form:file element that maps to your ActionForm's FormFile property
-->
<f:view>
<f:loadBundle var="text" basename="#{fileUpload.bundleName}"/>

<h:form id="uploadForm" enctype="multipart/form-data" onsubmit="return validateUploadForm(this)">
<ul>
    <li class="info"><fmt:message key="upload.message"/></li>
    <li>
        <h:outputLabel for="name" value="#{text['uploadForm.name']}"/>
        <t:message for="name" styleClass="fieldError"/>
        <h:inputText value="#{fileUpload.name}" id="name" size="40" required="true" styleClass="text medium">
            <v:commonsValidator type="required" arg="#{text['uploadForm.name']}"/>
        </h:inputText>
    </li>
    <li>
        <h:outputLabel for="uploadForm:file" value="#{text['uploadForm.file']}"/>
        <t:message for="uploadForm:file" styleClass="fieldError"/>
        <t:inputFileUpload id="file" value="#{fileUpload.file}" storage="file" required="true" size="50" styleClass="file medium">
            <v:commonsValidator type="required" arg="#{text['uploadForm.file']}"/>
        </t:inputFileUpload>
    </li>
    <li class="buttonBar bottom">
        <h:commandButton value="#{text['button.upload']}" action="#{fileUpload.upload}"
            id="upload" styleClass="button"/>
        <h:commandButton value="#{text['button.cancel']}" action="mainMenu" immediate="true"
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </li>
</ul>
</h:form>

<v:validatorScript functionName="validateUploadForm"/>

<script type="text/javascript">
    Form.focusFirstElement($('uploadForm'));
    highlightFormElements();
</script>

</f:view>
