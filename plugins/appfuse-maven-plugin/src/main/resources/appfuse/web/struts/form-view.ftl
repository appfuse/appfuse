<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign dateExists = false>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="${pojoNameLower}Detail.title"/></title>
    <meta name="menu" content="${pojo.shortName}Menu"/>
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
    <s:form id="${pojoNameLower}Form" action="save${pojo.shortName}" method="post" validate="true" cssClass="well">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if field.equals(pojo.identifierProperty)>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
        <#lt/>        <s:textfield cssClass="form-control" key="${pojoNameLower}.${field.name}" required="true" autofocus="true"/>
    <#else>
        <s:hidden key="${pojoNameLower}.${field.name}"/>
    </#if>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#foreach column in field.getColumnIterator()>
        <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
            <#assign dateExists = true>
            <#lt/>        <s:textfield cssClass="form-control date" key="${pojoNameLower}.${field.name}" required="${(!column.nullable)?string}" <#if (column.length > 0)>maxlength="${column.length?c}" </#if>size="11" title="date" datepicker="true"/>
        <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
            <#lt/>        <s:checkbox key="${pojoNameLower}.${field.name}" theme="css_xhtml"/>
        <#else>
            <#lt/>        <s:textfield cssClass="form-control" key="${pojoNameLower}.${field.name}" required="${(!column.nullable)?string}" <#if (column.length > 0)>maxlength="${column.length?c}" </#if>/>
        </#if>
    </#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
            <#lt/>        <!-- todo: change this to read the identifier field from the other pojo -->
            <#lt/>        <s:select name="${pojoNameLower}.${field.name}.id" list="${field.name}List" listKey="id" listValue="id" cssClass="form-control"></s:select>
    </#foreach>
</#if>
</#foreach>

        <div class="form-group">
            <s:submit type="button" id="save" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
                <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
            </s:submit>
            <c:if test="${'$'}{not empty ${pojoNameLower}.${idFieldName}}">
                <s:submit type="button" id="delete" cssClass="btn btn-danger" method="delete" key="button.delete"
                    onclick="return confirmMessage(msgDelConfirm)" theme="simple">
                    <i class="icon-trash icon-white"></i> <fmt:message key="button.delete"/>
                </s:submit>
            </c:if>
            <a href="${'$'}{ctx}/${util.getPluralForWord(pojoNameLower)}" class="btn btn-default">
                <i class="icon-remove"></i> <fmt:message key="button.cancel"/></a>
        </div>
    </s:form>
</div>

<#if dateExists><#rt/>
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/webjars/bootstrap-datepicker/1.3.1/css/datepicker.css'/>" />
<script type="text/javascript" src="<c:url value='/webjars/bootstrap-datepicker/1.3.1/js/bootstrap-datepicker.js'/>"></script>
<c:if test="${'$'}{pageContext.request.locale.language != 'en'}">
<script type="text/javascript" src="<c:url value='/webjars/bootstrap-datepicker/1.3.1/js/locales/bootstrap-datepicker.${r"${pageContext.request.locale.language}"}.js'/>"></script>
</c:if>
</#if><#rt/>
<#if dateExists>
<script type="text/javascript">
    $(document).ready(function() {
        ${'$'}('.date').datepicker({format: "<fmt:message key='calendar.format'/>", weekStart: "<fmt:message key='calendar.weekstart'/>", language: '${r"${pageContext.request.locale.language}"}'});
    });
</script>
</#if>
