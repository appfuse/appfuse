<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign dateExists = false>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="${pojoNameLower}Detail.title"/></title>
    <meta name="menu" content="${pojo.shortName}Menu"/>
    <meta name="heading" content="<fmt:message key='${pojoNameLower}Detail.heading'/>"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="${pojoNameLower}List.${pojoNameLower}"/></c:set>
<script type="text/javascript">var msgDelConfirm =
   "<fmt:message key="delete.confirm"><fmt:param value=${'"'}${r"${delObject}"}${'"'}/></fmt:message>";
</script>

<div class="col-sm-3">
    <h2><fmt:message key="${pojoNameLower}Detail.heading"/></h2>
    <fmt:message key="${pojoNameLower}Detail.message"/>
</div>

<div class="col-sm-6">
<form:errors path="*" cssClass="alert alert-danger alert-dismissable" element="div"/>
<form:form commandName="${pojoNameLower}" method="post" action="${pojoNameLower}form" cssClass="well"
           id="${pojoNameLower}Form" onsubmit="return validate${pojo.shortName}(this)">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if field.equals(pojo.identifierProperty)>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
        <#lt/>
    <spring:bind path="${pojoNameLower}.${field.name}">
    <div class="form-group${'$'}{(not empty status.errorMessage) ? ' has-error' : ''}">
    </spring:bind>
        <appfuse:label key="${pojoNameLower}.${field.name}" styleClass="control-label"/>
        <form:input path="${field.name}" id="${field.name}"/>
        <form:errors path="${field.name}" cssClass="help-block"/>
    </div>
    <#else>
        <#lt/><form:hidden path="${field.name}"/>
    </#if>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#foreach column in field.getColumnIterator()>
    <spring:bind path="${pojoNameLower}.${field.name}">
    <div class="form-group${'$'}{(not empty status.errorMessage) ? ' has-error' : ''}">
    </spring:bind>
        <appfuse:label key="${pojoNameLower}.${field.name}" styleClass="control-label"/>
        <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
        <#assign dateExists = true/>
        <form:input cssClass="form-control" path="${field.name}" id="${field.name}" size="11" title="date" datepicker="true"/>
        <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
        <form:checkbox path="${field.name}" id="${field.name}" cssClass="checkbox"/>
        <#else>
        <form:input cssClass="form-control" path="${field.name}" id="${field.name}" <#if (column.length > 0)> maxlength="${column.length?c}"</#if>/>
        </#if>
        <form:errors path="${field.name}" cssClass="help-block"/>
    </div>
    </#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
            <#lt/>    <!-- todo: change this to read the identifier field from the other pojo -->
            <#lt/>    <form:select cssClass="form-control" path="${field.name}" items="${field.name}List" itemLabel="label" itemValue="value"/>
    </#foreach>
</#if>
</#foreach>

    <div class="form-group">
        <button type="submit" class="btn btn-primary" id="save" name="save" onclick="bCancel=false">
            <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
        </button>
        <c:if test="${'$'}{not empty ${pojoNameLower}.${idFieldName}}">
            <button type="submit" class="btn btn-danger" id="delete" name="delete" onclick="bCancel=true;return confirmMessage(msgDelConfirm)">
                <i class="icon-trash icon-white"></i> <fmt:message key="button.delete"/>
            </button>
        </c:if>

        <button type="submit" class="btn btn-default" id="cancel" name="cancel" onclick="bCancel=true">
            <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
        </button>
    </div>
</form:form>
</div>

<v:javascript formName="${pojoNameLower}" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value='/scripts/validator.jsp'/>"></script>

<#if dateExists><#rt/>
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/webjars/bootstrap-datepicker/1.3.1/css/datepicker.css'/>" />
<script type="text/javascript" src="<c:url value='/webjars/bootstrap-datepicker/1.3.1/js/bootstrap-datepicker.js'/>"></script>
<c:if test="${'$'}{pageContext.request.locale.language != 'en'}">
<script type="text/javascript" src="<c:url value='/webjars/bootstrap-datepicker/1.3.1/js/locales/bootstrap-datepicker.${r"${pageContext.request.locale.language}"}.js'/>"></script>
</c:if>
</#if><#rt/>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['${pojoNameLower}Form']).focus();
<#if dateExists>
        ${'$'}('.text-right.date').datepicker({format: "<fmt:message key='calendar.format'/>", weekStart: "<fmt:message key='calendar.weekstart'/>", language: '${r"${pageContext.request.locale.language}"}'});
</#if>
    });
</script>
