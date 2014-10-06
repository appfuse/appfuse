<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="upload.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<div class="col-sm-3">
    <h2><fmt:message key="upload.heading"/></h2>
    <p><fmt:message key="upload.message"/></p>
</div>
<div class="col-sm-7">
    <s:form action="uploadFile" enctype="multipart/form-data" method="post" validate="true" id="uploadForm" cssClass="well">
        <s:textfield name="name" label="%{getText('uploadForm.name')}" required="true" autofocus="true" cssClass="form-control"/>
        <s:file name="file" label="%{getText('uploadForm.file')}" required="true"/>
        <div id="actions" class="form-group">
            <s:submit type="button" key="button.upload" name="upload" cssClass="btn btn-primary" theme="simple">
                <i class="icon-upload icon-white"></i>
                <fmt:message key="button.upload"/>
            </s:submit>

            <a class="btn btn-default" href="home" >
                <i class="icon-remove"></i>
                <fmt:message key="button.cancel"/>
            </a>
        </div>
    </s:form>
</div>