<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign dateExists = false>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="${pojoNameLower}Detail.title"/></title>
    <meta name="menu" content="${pojo.shortName}Menu"/>
</head>

<div class="col-sm-3">
    <h2><fmt:message key="${pojoNameLower}Detail.heading"/></h2>
    <fmt:message key="${pojoNameLower}Detail.message"/>
</div>
<div class="col-sm-6">
    <stripes:errors globalErrorsOnly="true"/>

    <stripes:form action="/${pojoNameLower}form.action" id="${pojoNameLower}Form" class="well">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
    <#assign isDate = false>
    <#assign isBoolean = false>
    <#if field.equals(pojo.identifierProperty)>
        <#assign idFieldName = field.name>
        <#if field.value.identifierGeneratorStrategy == "assigned">
            <#lt/>
            <div class="form-group${'$'}{empty(actionBean.context.validationErrors['${pojoNameLower}.${field.name}']) ? '' : ' has-error'}">
                <label for="${field.name}" class="control-label"><fmt:message key="${pojoNameLower}.${field.name}"/>:</label>
                <stripes:text name="${pojoNameLower}.${field.name}" id="${field.name}" class="form-control"/>
                <stripes:errors field="${pojoNameLower}.${field.name}"/>
            </div>
        </div>
        <#else>
            <#lt/>      <stripes:hidden name="${pojoNameLower}.${field.name}"/>
        </#if>
    <#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
        <#foreach column in field.getColumnIterator()>
        <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
            <#assign isDate = true>
        </#if>
        <#if field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
            <#assign isBoolean = true>
        </#if>
        <div class="form-group${'$'}{empty(actionBean.context.validationErrors['${pojoNameLower}.${field.name}']) ? '' : ' has-error'}<#if isBoolean> checkbox</#if>">
        <#if isBoolean == false>
            <label for="${field.name}" class="control-label"><fmt:message key="${pojoNameLower}.${field.name}"/>:</label>
        <#else>
            <label for="${field.name}">
        </#if>
        <#if isDate>
            <#assign dateExists = true/>
            <stripes:text class="form-control date" name="${pojoNameLower}.${field.name}" id="${field.name}" size="11" title="date"/>
        <#elseif isBoolean>
            <stripes:checkbox name="${pojoNameLower}.${field.name}" id="${field.name}"/>
            <fmt:message key="${pojoNameLower}.${field.name}"/></label>
        <#else>
            <stripes:text class="form-control" name="${pojoNameLower}.${field.name}" id="${field.name}" <#if (column.length > 0)> maxlength="${column.length?c}"</#if>/>
        </#if>
            <stripes:errors field="${pojoNameLower}.${field.name}"/>
        </div>
        </#foreach>
    </#if>
</#foreach>
        <div class="form-group">
            <button type="submit" class="btn btn-primary" name="save" id="save">
                <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
            </button>

            <c:if test="${'$'}{not empty param.${idFieldName}}">
                <button type="submit" class="btn btn-danger" name="delete" id="delete">
                    <i class="icon-trash"></i> <fmt:message key="button.delete"/>
                </button>
            </c:if>

            <a href="${'$'}{ctx}/${util.getPluralForWord(pojoNameLower)}" class="btn btn-default" id="cancel">
                <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
            </a>
        </div>
    </stripes:form>
</div>

<#if dateExists><#rt/>
<link rel="stylesheet" type="text/css" media="all" href="${'$'}{ctx}/webjars/bootstrap-datepicker/1.3.1/css/datepicker.css" />
<script type="text/javascript" src="${'$'}{ctx}/webjars/bootstrap-datepicker/1.3.1/js/bootstrap-datepicker.js"></script>
<c:if test="${'$'}{pageContext.request.locale.language != 'en'}">
<script type="text/javascript" src="${'$'}{ctx}/webjars/bootstrap-datepicker/1.3.1/js/locales/bootstrap-datepicker.${r"${pageContext.request.locale.language}"}.js'/>"></script>
</c:if>
</#if><#rt/>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['${pojoNameLower}Form']).focus();
    <#if dateExists>
        $('.date').datepicker({format: "<fmt:message key='calendar.format'/>", weekStart: "<fmt:message key='calendar.weekstart'/>", language: '${r"${pageContext.request.locale.language}"}'});
    </#if>
    });
</script>

