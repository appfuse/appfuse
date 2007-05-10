<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://java.sun.com/jstl/core"
      xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets" xmlns:t="http://myfaces.apache.org/tomahawk">

<f:view>
<f:loadBundle var="text" basename="${'#'}{${pojo.shortName.toLowerCase()}List.bundleName}"/>
    <head>
        <title>${'#'}{text['${pojo.shortName.toLowerCase()}List.title']}</title>
        <meta name="heading" content="${'#'}{text['${pojo.shortName.toLowerCase()}List.heading']}"/>
        <meta name="menu" content="${pojo.shortName}Menu"/>
    </head>
<body id="${pojo.shortName.toLowerCase()}List">
<h:form id="edit${pojo.shortName}">

<h:commandButton value="${'#'}{text['button.add']}" action="add" id="add" immediate="true" styleClass="button"/>
<h:commandButton value="${'#'}{text['button.done']}" action="mainMenu" id="cancel" immediate="true" styleClass="button" style="margin-left: 5px"/>

<!-- Error from this table is caused by http://issues.apache.org/jira/browse/TOMAHAWK-466 -->
<t:dataTable id="${pojo.shortName.toLowerCase()}s" var="${pojo.shortName.toLowerCase()}" style="margin-top: 10px"
    value="${'#'}{${pojo.shortName.toLowerCase()}List.${pojo.shortName.toLowerCase()}s}" rows="25" sortColumn="${'#'}{${pojo.shortName.toLowerCase()}List.sortColumn}"
    sortAscending="${'#'}{${pojo.shortName.toLowerCase()}List.ascending}" styleClass="scrollerTable table"
    headerClass="standardTable_Header" rowClasses="standardTable_Row1,standardTable_Row2"
    columnClasses="standardTable_Column,standardTable_Column,standardTable_Column,standardTable_Column,standardTable_ColumnCentered">

    <t:column>
        <f:facet name="header">
            <t:commandSortHeader columnName="id" arrow="true">
                <h:outputText value="${'#'}{text['${pojo.shortName.toLowerCase()}.id']}" />
            </t:commandSortHeader>
        </f:facet>
        <h:commandLink action="${'#'}{${pojo.shortName.toLowerCase()}Form.edit}" value="${'#'}{${pojo.shortName.toLowerCase()}.id}">
            <f:param name="id" value="${'#'}{${pojo.shortName.toLowerCase()}.id}"/>
            <f:param name="from" value="list"/>
        </h:commandLink>
    </t:column>

    <t:column>
        <f:facet name="header">
            <t:commandSortHeader columnName="firstName" arrow="true">
                <h:outputText value="${'#'}{text['${pojo.shortName.toLowerCase()}.firstName']}" />
            </t:commandSortHeader>
        </f:facet>
        <h:outputText value="${'#'}{${pojo.shortName.toLowerCase()}.firstName}" escape="true"/>
    </t:column>

    <t:column>
        <f:facet name="header">
            <t:commandSortHeader columnName="lastName" arrow="true">
                <h:outputText value="${'#'}{text['${pojo.shortName.toLowerCase()}.lastName']}" />
            </t:commandSortHeader>
        </f:facet>
        <h:outputText value="${'#'}{${pojo.shortName.toLowerCase()}.lastName}" escape="true"/>
    </t:column>

</t:dataTable>

<ui:include src="/common/tableFooter.xhtml">
    <ui:param name="tableName" value="${pojo.shortName.toLowerCase()}s"/>
</ui:include>

<script type="text/javascript">
    highlightTableRows("edit${pojo.shortName}:${pojo.shortName.toLowerCase()}s");
</script>

</h:form>
</body>
</f:view>
</html> 