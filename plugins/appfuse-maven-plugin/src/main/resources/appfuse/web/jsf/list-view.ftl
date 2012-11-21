<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://java.sun.com/jstl/core"
      xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets" xmlns:t="http://myfaces.apache.org/tomahawk">

<f:view>
<f:loadBundle var="text" basename="${'#'}{${pojoNameLower}List.bundleName}"/>
    <head>
        <title>${'#'}{text['${pojoNameLower}List.title']}</title>
        <meta name="heading" content="${'#'}{text['${pojoNameLower}List.heading']}"/>
        <meta name="menu" content="${pojo.shortName}Menu"/>
    </head>
<body id="${pojoNameLower}List">

<div class="span10">
    <h2>${'#'}{text['${pojoNameLower}List.heading']}</h2>

    <h:form id="searchForm" styleClass="form-search">
    <div id="search" class="input-append">
        <h:inputText id="q" name="q" size="20" value="${'#'}{${pojoNameLower}List.query}" styleClass="input-medium search-query"/>
        <h:commandButton value="${'#'}{text['button.search']}" styleClass="btn" action="${'#'}{${pojoNameLower}List.search}"/>
    </div>
    </h:form>

    <p>${'#'}{text['${pojoNameLower}List.message']}</p>

    <h:form id="edit${pojo.shortName}">

    <div id="actions" class="form-actions">
        <h:commandButton value="${'#'}{text['button.add']}" action="add" id="add" immediate="true" styleClass="btn btn-primary"/>
        <h:commandButton value="${'#'}{text['button.done']}" action="mainMenu" id="cancel" immediate="true" styleClass="btn"/>
    </div>

    <t:dataTable id="${util.getPluralForWord(pojoNameLower)}" var="${pojoNameLower}"
        value="${'#'}{${pojoNameLower}List.${util.getPluralForWord(pojoNameLower)}}" rows="25" sortColumn="${'#'}{${pojoNameLower}List.sortColumn}"
        sortAscending="${'#'}{${pojoNameLower}List.ascending}" styleClass="table table-condensed table-striped table-hover"
        headerClass="standardTable_Header" rowClasses="standardTable_Row1,standardTable_Row2"
        columnClasses="standardTable_Column,standardTable_Column,standardTable_Column,standardTable_Column,standardTable_ColumnCentered">

    <#foreach field in pojo.getAllPropertiesIterator()>
        <t:column>
            <f:facet name="header">
                <t:commandSortHeader columnName="${field.name}" arrow="true">
                    <h:outputText value="${'#'}{text['${pojoNameLower}.${field.name}']}" />
                </t:commandSortHeader>
            </f:facet>
    <#if field.equals(pojo.identifierProperty)>
            <h:commandLink action="${'#'}{${pojoNameLower}Form.edit}" value="${'#'}{${pojoNameLower}.${field.name}}">
                <f:param name="${field.name}" value="${'#'}{${pojoNameLower}.${field.name}}"/>
                <f:param name="from" value="list"/>
            </h:commandLink>
    <#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
        <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
            <#lt/>    <h:outputText value="${'#'}{${pojoNameLower}.${field.name}}" escape="true"/>
        <#elseif field.value.typeName == "boolean">
            <#lt/>    <h:selectBooleanCheckbox value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}" disabled="disabled"/>
        <#else>
            <#lt/>    <h:outputText value="${'#'}{${pojoNameLower}.${field.name}}" escape="true"/>
        </#if>
    </#if>
        </t:column>
    </#foreach>
    </t:dataTable>

    <ui:include src="/common/tableFooter.xhtml">
        <ui:param name="tableName" value="${util.getPluralForWord(pojoNameLower)}"/>
    </ui:include>

    </h:form>
</div>
</body>
</f:view>
</html> 