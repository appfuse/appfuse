<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>

<head>
    <title>${'$'}{rc.getMessage("${pojoNameLower}List.title")}</title>
    <meta name="menu" content="${pojo.shortName}Menu"/>
</head>

<h2>${'$'}{rc.getMessage("${pojoNameLower}List.title")}</h2>

<form method="get" action="${'$'}{rc.contextPath}/${util.getPluralForWord(pojoNameLower)}" id="searchForm" class="form-inline">
<div id="search" class="text-right">
    <span class="col-sm-9">
        <input type="text" size="20" name="q" id="query" value="${'$'}{RequestParameters.q!''}"
               placeholder="${'$'}{rc.getMessage("search.enterTerms")}" class="form-control input-sm"/>
    </span>
    <button id="button.search" class="btn btn-default btn-sm" type="submit">
        <i class="icon-search"></i> ${'$'}{rc.getMessage("button.search")}
    </button>
</div>
</form>

<p>${'$'}{rc.getMessage("${pojoNameLower}List.message")}</p>

<div id="actions" class="btn-group">
    <a href="${pojoNameLower}form" class="btn btn-primary">
        <i class="icon-plus icon-white"></i> ${'$'}{rc.getMessage("button.add")}</a>
    <a href="home" class="btn btn-default"><i class="icon-ok"></i> ${'$'}{rc.getMessage("button.done")}</a>
</div>

<table class="table table-condensed table-striped table-hover" id="${pojoNameLower}List">
    <thead>
    <tr>
<#foreach field in pojo.getAllPropertiesIterator()>
        <th>${'$'}{rc.getMessage("${pojoNameLower}.${field.name}")}</th>
</#foreach>
    </tr>
    </thead>
    <tbody>
    ${'<#'}list ${pojoNameLower}List as ${pojoNameLower}>
        <tr>
<#foreach field in pojo.getAllPropertiesIterator()>
    <#if field.equals(pojo.identifierProperty)>
        <td><a href="${'$'}{rc.contextPath}/${pojoNameLower}form?id=${'$'}{${pojoNameLower}.${field.name}}">${'$'}{${pojoNameLower}.${field.name}}</a></td>
    <#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
        <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
        <#lt/>            <td>${'$'}{${pojoNameLower}.${field.name}?html}</td>
        <#elseif field.value.typeName == "boolean">
        <#lt/>            <td><input type="checkbox" disabled="disabled" ${'<#if'} ${pojoNameLower}.${field.name}>checked="checked"${'</#if>'}/></td>
        <#else>
        <#lt/>            <td>${'$'}{${pojoNameLower}.${field.name}?html}</td>
        </#if>
    </#if>
</#foreach>
        </tr>
    ${'</#list>'}
    </tbody>
</table>
