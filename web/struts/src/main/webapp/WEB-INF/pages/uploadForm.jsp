<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="upload.title"/></title>
    <meta name="heading" content="<fmt:message key='upload.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<div class="row">
    <div class="span3">
        <fmt:message key="upload.message"/>
    </div>
    <div class="span9">
        <s:form action="uploadFile!upload" enctype="multipart/form-data" method="post" validate="true" id="uploadForm"
                cssClass="well form-horizontal">
            <s:textfield name="name" label="%{getText('uploadForm.name')}" required="true"/>
            <s:file name="file" label="%{getText('uploadForm.file')}" required="true"/>
            <fieldset class="form-actions">
                <s:submit key="button.upload" name="upload" cssClass="btn primary" theme="simple"/>
                <input type="button" value="<fmt:message key="button.cancel"/>" class="btn"
                    onclick="this.form.onsubmit = null; location.href='mainMenu'"/>
            </fieldset>
        </s:form>
    </div>
</div>
