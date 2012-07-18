<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="${pojoNameLower}List.title"/></title>
    <meta name="heading" content="<fmt:message key='${pojoNameLower}List.heading'/>"/>
    <meta name="menu" content="${pojo.shortName}Menu"/>
</head>

<div class="span10">
    <h2><fmt:message key="${pojoNameLower}List.heading"/></h2>
    <div id="search">
        <form method="get" action="${'$'}{ctx}/${util.getPluralForWord(pojoNameLower)}" id="searchForm" class="form-search">
            <input type="text" size="20" name="q" id="query" value="${'$'}{param.q}"
                   placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
            <button id="button.search" class="btn" type="submit">
                <i class="icon-search"></i>
                <fmt:message key="button.search"/>
            </button>
        </form>
    </div>
    <fmt:message key="${pojoNameLower}List.message"/>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/edit${pojo.shortName}'/>" >
            <i class="icon-plus"></i>
            <fmt:message key="button.add"/>
        </a>
        <a class="btn" href="<c:url value="/mainMenu"/>" >
            <fmt:message key="button.done"/>
        </a>
    </div>

    <display:table name="${util.getPluralForWord(pojoNameLower)}" class="table" requestURI="" id="${pojoNameLower}List" export="true" pagesize="25">
<#foreach field in pojo.getAllPropertiesIterator()>
<#if field.equals(pojo.identifierProperty)>
        <display:column property="${field.name}" sortable="true" href="edit${pojo.shortName}" media="html"
            paramId="${field.name}" paramProperty="${field.name}" titleKey="${pojoNameLower}.${field.name}"/>
        <display:column property="${field.name}" media="csv excel xml pdf" titleKey="${pojoNameLower}.${field.name}"/>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#if field.value.typeName == "java.util.Date">
        <#lt/>        <display:column sortProperty="${field.name}" sortable="true" titleKey="${pojoNameLower}.${field.name}">
        <#lt/>             <fmt:formatDate value="${'$'}{${pojoNameLower}List.${field.name}}" pattern="${'$'}{datePattern}"/>
        <#lt/>        </display:column>
    <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
        <#lt/>        <display:column sortProperty="${field.name}" sortable="true" titleKey="${pojoNameLower}.${field.name}">
        <#lt/>            <input type="checkbox" disabled="disabled" <c:if test="${'$'}{${pojoNameLower}List.${field.name}}">checked="checked"</c:if>/>
        <#lt/>        </display:column>
    <#else>
        <#lt/>        <display:column property="${field.name}" sortable="true" titleKey="${pojoNameLower}.${field.name}"/>
    </#if>
</#if>
</#foreach>

        <display:setProperty name="paging.banner.item_name"><fmt:message key="${pojoNameLower}List.${pojoNameLower}"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="${pojoNameLower}List.${util.getPluralForWord(pojoNameLower)}"/></display:setProperty>

        <display:setProperty name="export.excel.filename"><fmt:message key="${pojoNameLower}List.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="${pojoNameLower}List.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="${pojoNameLower}List.title"/>.pdf</display:setProperty>
    </display:table>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/edit${pojo.shortName}'/>" >
            <i class="icon-plus"></i>
            <fmt:message key="button.add"/>
        </a>
        <a class="btn" href="<c:url value="/mainMenu"/>" >
            <fmt:message key="button.done"/>
        </a>
    </div>
</div>

<script type="text/javascript">
    highlightTableRows("${pojoNameLower}List");
</script>
