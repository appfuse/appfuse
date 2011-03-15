<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://java.sun.com/jstl/core"
      xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets" xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:v="http://shale.apache.org/validator">

<f:view>
<f:loadBundle var="text" basename="${'#'}{${pojoNameLower}Form.bundleName}"/>
    <head>
        <title>${'#'}{text['${pojoNameLower}Detail.title']}</title>
        <meta name="heading" content="${'#'}{text['${pojoNameLower}Detail.heading']}"/>
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
        <v:commonsValidator client="true" type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    </h:inputText>
    <t:message for="${field.name}" styleClass="fieldError"/>
    <#else>
        <#lt/><h:inputHidden value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}"/>
<h:panelGrid columns="3">
    </#if>
    </#foreach>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <h:outputLabel styleClass="desc" for="${field.name}" value="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    <t:message for="${field.name}" styleClass="fieldError"/>
    <#foreach column in field.getColumnIterator()>
        <#if field.value.typeName == "java.util.Date">
            <#lt/>    <h:inputText styleClass="text medium" id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}">
        <#if !column.nullable>
        <v:commonsValidator client="true" type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
        </#if>
        <f:convertDateTime pattern="${'#'}{text['date.format']}"/>
    </h:inputText>
        <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
            <#lt/>    <h:selectBooleanCheckbox value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}" styleClass="checkbox"/>
        <#else>
            <#lt/>    <h:inputText styleClass="text medium" id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}"<#if (column.length > 0)> maxlength="${column.length?c}"</#if>>
        <#if !column.nullable>
        <v:commonsValidator client="true" type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
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
        <v:commonsValidator client="true" type="required" arg="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
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
