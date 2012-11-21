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
    <spring:bind path="fileUpload.*">
        <c:if test="${not empty status.errorMessages}">
        <div class="alert alert-error fade in">
            <a href="#" data-dismiss="alert" class="close">&times;</a>
            <c:forEach var="error" items="${status.errorMessages}">
                <c:out value="${error}" escapeXml="false"/><br />
            </c:forEach>
        </div>
        </c:if>
    </spring:bind>

    <form:form commandName="fileUpload" method="post" action="fileupload" enctype="multipart/form-data"
        onsubmit="return validateFileUpload(this)" id="uploadForm" cssClass="well form-horizontal">
        <spring:bind path="fileUpload.name">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label key="uploadForm.name" styleClass="control-label"/>
            <div class="controls">
                <form:input path="name" id="name"/>
                <form:errors path="name" cssClass="help-inline"/>
            </div>
        </fieldset>
        <spring:bind path="fileUpload.file">
        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        </spring:bind>
            <appfuse:label key="uploadForm.file" styleClass="control-label"/>
            <div class="controls">
                <input type="file" name="file" id="file"/>
                <form:errors path="file" cssClass="help-inline"/>
            </div>
        </fieldset>
        <fieldset class="form-actions">
            <button type="submit" name="upload" class="btn btn-primary" onclick="bCancel=false">
                <i class="icon-upload icon-white"></i> <fmt:message key="button.upload"/>
            </button>
            <button type="submit" name="cancel" class="btn" onclick="bCancel=true">
                <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
            </button>
        </fieldset>
    </form:form>
</div>
<c:set var="scripts" scope="request">
<v:javascript formName="fileUpload" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
</c:set>