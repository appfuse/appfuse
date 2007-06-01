<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://java.sun.com/jstl/core"
      xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets" xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:v="http://corejsf.com/validator">

<f:view>
<f:loadBundle var="text" basename="${'#'}{${pojoNameLower}Form.bundleName}"/>
    <head>
        <title>${'#'}{text['${pojoNameLower}Detail.title']}</title>
        <meta name="heading" content="${'#'}{text['${pojoNameLower}Detail.heading']}"/>
        <style type="text/css">
            .jscalendar-DB-div-style {
                position: absolute;
                margin-top: -120px;
                margin-left: -508px;
            }
    </style>
    </head>
<body id="${pojoNameLower}Form">

<h:form id="${pojoNameLower}Form" onsubmit="return validate${pojo.shortName}Form(this)">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if field.equals(pojo.identifierProperty)>
    <#foreach column in field.getColumnIterator()>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
<h:panelGrid columns="3">
    <h:outputLabel styleClass="desc" for="${field.name}" value="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    <h:inputText styleClass="text medium" id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}">
        <v:commonsValidator type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    </h:inputText>
    <t:message for="${field.name}" styleClass="fieldError"/>
    <#else>
        <#lt/><h:inputHidden value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}"/>
<h:panelGrid columns="3">
    </#if>
    </#foreach>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field)>
    <h:outputLabel styleClass="desc" for="${field.name}" value="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    <t:message for="${field.name}" styleClass="fieldError"/>
    <#foreach column in field.getColumnIterator()>
        <#if field.value.typeName == "java.util.Date">
            <#lt/>    <t:inputCalendar monthYearRowClass="yearMonthHeader" weekRowClass="weekHeader" id="${field.name}"
            currentDayCellClass="currentDayCell" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}"
            renderAsPopup="true" addResources="true" required="${(!column.nullable)?string}">
        <#if !column.nullable>
        <v:commonsValidator type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
        </#if>
    </t:inputCalendar>
        <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
            <#lt/>    <h:selectBooleanCheckbox value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}" styleClass="checkbox"/>
        <#else>
            <#lt/>    <h:inputText styleClass="text medium" id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}">
        <#if !column.nullable>
        <v:commonsValidator type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
        </#if>
    </h:inputText>
        </#if>
    </#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
            <#lt/>    <!-- todo: change this to read the identifier field from the other pojo -->
            <#lt/>    <h:selectOneMenu value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}" required="${(!column.nullable)?string}" styleClass="select">
        <f:selectItems value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}"/>
        <#if !column.nullable>
        <v:commonsValidator type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
        </#if>
    </h:selectOneMenu>
    </#foreach>
</#if>
</#foreach>

    <h:panelGroup styleClass="buttonBar bottom">
        <h:commandButton value="${'#'}{text['button.save']}" action="${'#'}{${pojoNameLower}Form.save}" id="save" styleClass="button"/>

        <c:if test="${'$'}{not empty ${pojoNameLower}Form.${pojoNameLower}.${idFieldName}}">
        <h:commandButton value="${'#'}{text['button.delete']}" action="${'#'}{${pojoNameLower}Form.delete}"
            id="delete" styleClass="button" onclick="bCancel=true; return confirmDelete('${pojo.shortName}')"/>
        </c:if>

        <h:commandButton value="${'#'}{text['button.cancel']}" action="cancel" immediate="true"
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
    <h:outputText/><h:outputText/>
</h:panelGrid>
</h:form>

<v:validatorScript functionName="validate${pojo.shortName}Form"/>

<script type="text/javascript">
    //Form.focusFirstElement($('${pojoNameLower}Form'));
    highlightFormElements();
</script>

</body>
</f:view>
</html>
