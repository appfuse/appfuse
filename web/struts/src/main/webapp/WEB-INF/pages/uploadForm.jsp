<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="upload.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<div class="span3">
    <h2><fmt:message key="upload.heading"/></h2>
    <p><fmt:message key="upload.message"/></p>
</div>
<div class="span7">
    <s:form action="uploadFile!upload" enctype="multipart/form-data" method="post" validate="true" id="uploadForm"
            cssClass="well form-horizontal">
        <s:textfield name="name" label="%{getText('uploadForm.name')}" required="true"/>
        <s:file name="file" label="%{getText('uploadForm.file')}" required="true"/>
        <div id="actions" class="form-actions">
            <s:submit type="button" key="button.upload" name="upload" cssClass="btn btn-primary" theme="simple">
                <i class="icon-upload icon-white"></i>
                <fmt:message key="button.upload"/>
            </s:submit>

            <a class="btn" href="mainMenu" >
                <i class="icon-remove"></i>
                <fmt:message key="button.cancel"/>
            </a>
        </div>
    </s:form>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['uploadForm']).focus();
    });
</script>
