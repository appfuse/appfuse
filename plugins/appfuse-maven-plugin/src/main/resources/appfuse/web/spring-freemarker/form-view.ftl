<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
${'<#assign dateExists = false>'}
${'<#import "/spring.ftl" as spring/>'}
${'<#assign xhtmlCompliant = true in spring>'}

<head>
    <title>${'<@spring'}.message "${pojoNameLower}Detail.title"/></title>
    <meta name="menu" content="${pojo.shortName}Menu"/>
</head>

<div class="col-sm-3">
    <h2>${'<@spring'}.message "${pojoNameLower}Detail.heading"/></h2>
    ${'<@spring'}.message "${pojoNameLower}Detail.message"/>
</div>

<div class="col-sm-6">
${'<@spring'}.bind "${pojoNameLower}.*"/>
${'<#if'} spring.status.error>
    <div class="alert alert-danger alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        ${'<#list'} spring.status.errorMessages as error>
        ${'$'}{error}<br/>
        ${'</#list>'}
    </div>
${'</#if>'}

<form method="post" action="${'<@spring'}.url '/${pojoNameLower}form'/>" id="${pojoNameLower}Form" class="well" autocomplete="off">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<#assign isDate = false>
<#assign isBoolean = false>
<#if field.equals(pojo.identifierProperty)>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
        <#lt/><ul>
    ${'<@spring'}.bind "${pojoNameLower}.${field.name}"/>
        <#if spring.status.error> has-error</#if>
    <div class="form-group${'<#if'} spring.status.error> has-error${'</#if>'}">
        <label for="${field.name}" class="control-label">${'<@spring'}.message "${pojoNameLower}.${field.name}"/>:</label>
        ${'<@spring'}.formInput "${pojoNameLower}.${field.name}", 'id="${field.name}" class="form-control"'/>
        ${'<@spring'}.showErrors "<br/>", "help-block"/>
    </div>
    <#else>
        <#lt/>${'<@spring'}.formHiddenInput "${pojoNameLower}.${field.name}"/>
    </#if>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#foreach column in field.getColumnIterator()>
    <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
        <#assign isDate = true>
    </#if>
    <#if field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
        <#assign isBoolean = true>
    </#if>
    ${'<@spring'}.bind "${pojoNameLower}.${field.name}"/>
    <div class="form-group${'<#if'} spring.status.error> has-error${'</#if>'}<#if isBoolean> checkbox</#if>">
        <#if isBoolean>
        <label for="${field.name}">
        <#else>
        <label for="${field.name}" class="control-label">${'<@spring'}.message "${pojoNameLower}.${field.name}"/>:</label>
        </#if>
        <#if isDate>
            <#assign dateExists = true/>
            ${'<@spring'}.formInput "${pojoNameLower}.${field.name}", 'id="${field.name}" size="11" title="date" class="form-control date" datepicker="true"'/>
        <#elseif isBoolean>
            ${'<@spring'}.formCheckbox "${pojoNameLower}.${field.name}", 'id="${field.name}"'/>
        <#else>
            ${'<@spring'}.formInput "${pojoNameLower}.${field.name}", 'id="${field.name}" class="form-control"<#if (column.length > 0)> maxlength="${column.length?c}"</#if>'/>
        </#if>
        <#if isBoolean>
        ${'<@spring'}.message "${pojoNameLower}.${field.name}"/></label>
        </#if>
            ${'<@spring'}.showErrors "<br/>", "help-block"/>
    </div>
    </#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
        <#lt/>    <!-- todo: change this to read the identifier field from the other pojo -->
        <#lt/>    <!--@spring.formSingleSelect "${pojoNameLower}.${field.name}", ${pojoNameLower}.${field.name}, 'class="form-control"'/-->
    </#foreach>
</#if>
</#foreach>

    <div class="form-group">
        <button type="submit" class="btn btn-primary" id="save" name="save">
            <i class="icon-ok icon-white"></i> ${'<@spring'}.message "button.save"/>
        </button>
        ${'<#if'} ${pojoNameLower}.${idFieldName}?exists>
            <button type="submit" class="btn btn-danger" id="delete" name="delete">
                <i class="icon-trash icon-white"></i> ${'<@spring'}.message "button.delete"/>
            </button>
        ${'</#if>'}
        <a href="${'$'}{rc.contextPath}/${util.getPluralForWord(pojoNameLower)}" class="btn btn-default">
            <i class="icon-remove"></i> ${'<@spring'}.message "button.cancel"/></a>
    </div>
</form>
</div>

<#if dateExists><#rt/>
<link rel="stylesheet" type="text/css" media="all" href="${'$'}{rc.contextPath}/webjars/bootstrap-datepicker/1.3.1/css/datepicker.css" />
<script type="text/javascript" src="${'$'}{rc.contextPath}/webjars/bootstrap-datepicker/1.3.1/js/bootstrap-datepicker.js"></script>
${'<#if'} rc.locale.language != 'en'>
<script type="text/javascript" src="${'$'}{rc.contextPath}/webjars/bootstrap-datepicker/1.3.1/js/locales/bootstrap-datepicker.${r"${rc.locale.language}"}.js'/>"></script>
${'</#if>'}
</#if><#rt/>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['${pojoNameLower}Form']).focus();
<#if dateExists>
        ${'$'}('.date').datepicker({format: "${'<@spring'}.message "calendar.format"/>", weekStart: "${'<@spring'}.message "calendar.weekstart"/>", language: '${r"${rc.locale.language}"}'});
</#if>
    });
</script>
