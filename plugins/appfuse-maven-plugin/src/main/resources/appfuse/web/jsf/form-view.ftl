<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://java.sun.com/jstl/core"
      xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets" xmlns:t="http://myfaces.apache.org/tomahawk">

<f:view>
<f:loadBundle var="text" basename="${'#'}{${pojoNameLower}Form.bundleName}"/>
    <head>
        <title>${'#'}{text['${pojoNameLower}Detail.title']}</title>
        <meta name="heading" content="${'#'}{text['${pojoNameLower}Detail.heading']}"/>
    </head>
<body id="${pojoNameLower}Form">

<c:set var="delObject" value="${'#'}{text['${pojoNameLower}List.${pojoNameLower}']}"/>
<script type="text/javascript">var msgDelConfirm =
   "<h:outputFormat value="${'#'}{text['delete.confirm']}"><f:param value="${'#'}{delObject}" /></h:outputFormat>";
</script>

<div class="span3">
    <h2>${'#'}{text['${pojoNameLower}Detail.heading']}</h2>
</div>
<div class="span7">

<h:form id="${pojoNameLower}Form">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if field.equals(pojo.identifierProperty)>
    <#foreach column in field.getColumnIterator()>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
<div class="control-group">
    <h:outputLabel styleClass="control-label" for="${field.name}" value="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    <div class="controls">
        <h:inputText id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}"/>
        <t:message for="${field.name}" styleClass="help-inline"/>
    <#else>
        <#lt/><h:inputHidden value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}"/>
    </div>
    </#if>
    </#foreach>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <h:outputLabel styleClass="control-label" for="${field.name}" value="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    <div class="controls">
    <#foreach column in field.getColumnIterator()>
        <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
            <#lt/>    <h:inputText id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}">
        <f:convertDateTime pattern="${'#'}{text['date.format']}"/>
    </h:inputText>
        <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
            <#lt/>    <h:selectBooleanCheckbox value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}"/>
        <#else>
            <#lt/>    <h:inputText id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}"<#if (column.length > 0)> maxlength="${column.length?c}"</#if>>
    </h:inputText>
        </#if>
    </#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
            <#lt/>    <!-- todo: change this to read the identifier field from the other pojo -->
            <#lt/>    <h:selectOneMenu value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}" required="${(!column.nullable)?string}" styleClass="select">
        <f:selectItems value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}"/>
    </h:selectOneMenu>
    </#foreach>
    <t:message for="${field.name}" styleClass="help-inline"/>
</#if>
</#foreach>
    </div>

    <div class="form-actions">
        <h:commandButton value="${'#'}{text['button.save']}" action="${'#'}{${pojoNameLower}Form.save}" id="save" styleClass="btn btn-primary"/>

        <c:if test="${'$'}{not empty ${pojoNameLower}Form.${pojoNameLower}.${idFieldName}}">
        <h:commandButton value="${'#'}{text['button.delete']}" action="${'#'}{${pojoNameLower}Form.delete}"
            id="delete" styleClass="btn" onclick="bCancel=true; return confirmMessage(msgDelConfirm)"/>
        </c:if>

        <h:commandButton value="${'#'}{text['button.cancel']}" action="cancel" immediate="true"
            id="cancel" styleClass="btn" onclick="bCancel=true"/>
    </div>
</div>
</h:form>

</body>
</f:view>
</html>
