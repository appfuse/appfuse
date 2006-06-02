<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="upload.title"/></title>
    <content tag="heading"><fmt:message key="upload.heading"/></content>
    <meta name="menu" content="FileUpload"/>
</head>

<!--
    The most important part is to declare your form's enctype to be "multipart/form-data",
    and to have a form:file element that maps to your ActionForm's FormFile property
-->
<f:view>
<f:loadBundle var="text" basename="#{fileUpload.bundleName}"/>

<h:form id="uploadForm" enctype="multipart/form-data" onsubmit="return validateUploadForm(this)">

<h:panelGrid columns="3">

    <h:panelGroup styleClass="info"><h:outputText value="#{text['upload.message']}"/></h:panelGroup>
    <h:outputText/><h:outputText/>
    
    <h:outputLabel styleClass="desc" for="name" value="#{text['uploadForm.name']}"/>
    <t:message for="name" styleClass="fieldError"/>
    <h:inputText value="#{fileUpload.name}" id="name" required="true" styleClass="text medium">
        <v:commonsValidator type="required" arg="#{text['uploadForm.name']}"/>
    </h:inputText>

    <h:outputLabel styleClass="desc" for="file" value="#{text['uploadForm.file']}"/>
    <t:message for="file" styleClass="fieldError"/>
    <t:inputFileUpload id="file" value="#{fileUpload.file}" storage="file" required="true" styleClass="file medium">
        <v:commonsValidator type="required" arg="#{text['uploadForm.file']}"/>
    </t:inputFileUpload>

    <h:panelGroup styleClass="buttonBar bottom">
        <h:commandButton value="#{text['button.upload']}" action="#{fileUpload.upload}"
            id="upload" styleClass="button"/>
        <h:commandButton value="#{text['button.cancel']}" action="mainMenu" immediate="true"
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
</h:panelGrid>
</h:form>

<v:validatorScript functionName="validateUploadForm"/>

<script type="text/javascript">
    Form.focusFirstElement($('uploadForm'));
    highlightFormElements();
</script>

</f:view>
