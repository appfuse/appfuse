<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.title"/></title>
<content tag="heading"><fmt:message key="upload.heading"/></content>

<!--
	The most important part is to declare your form's enctype to be "multipart/form-data",
	and to have a form:file element that maps to your ActionForm's FormFile property
-->
<f:view>
<f:loadBundle var="text" basename="#{fileUpload.bundleName}"/>

<fmt:message key="upload.message"/>
<div class="separator"></div>

<h:form id="uploadForm" enctype="multipart/form-data" onsubmit="return validateUploadForm(this)">               
<h:panelGrid columns="3" styleClass="detail" columnClasses="label">

    <h:outputLabel for="name" value="#{text['uploadForm.name']}"/>
    
    <h:inputText value="#{fileUpload.name}" id="name" size="40" required="true">
        <v:commonsValidator type="required" arg="#{text['uploadForm.name']}"/>
    </h:inputText>
    <t:message for="name" styleClass="fieldError"/>
    
    <h:outputLabel for="uploadForm:file" value="#{text['uploadForm.file']}"/>
    
    <t:inputFileUpload id="file" value="#{fileUpload.file}" storage="file" required="true" size="50">
        <v:commonsValidator type="required" arg="#{text['uploadForm.file']}"/>
    </t:inputFileUpload>
    <t:message for="uploadForm:file" styleClass="fieldError"/>
    
    <h:inputHidden value=""/>
    
    <h:panelGroup styleClass="buttonBar">
        <h:commandButton value="#{text['button.upload']}" action="#{fileUpload.upload}" 
            id="upload" styleClass="button"/>
        <h:commandButton value="#{text['button.cancel']}" action="mainMenu" immediate="true"  
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
    
    <h:inputHidden value=""/>
</h:panelGrid>
</h:form>

<v:validatorScript functionName="validateUploadForm"/>

<script type="text/javascript">highlightFormElements();</script>

</f:view>
