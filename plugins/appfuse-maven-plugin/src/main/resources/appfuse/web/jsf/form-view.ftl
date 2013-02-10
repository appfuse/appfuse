<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://java.sun.com/jsp/jstl/core"
      xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html"
      xmlns:p="http://primefaces.org/ui">

<f:view>
<f:loadBundle var="text" basename="${'#'}{${pojoNameLower}Form.bundleName}"/>
<h:head>
    <title>${'#'}{text['${pojoNameLower}Detail.title']}</title>
    <meta name="heading" content="${'#'}{text['${pojoNameLower}Detail.heading']}"/>
</h:head>

<c:set var="delObject" value="${'#'}{text['${pojoNameLower}List.${pojoNameLower}']}"/>
<script type="text/javascript">var msgDelConfirm =
   "<h:outputFormat value="${'#'}{text['delete.confirm']}"><f:param value="${'#'}{delObject}" /></h:outputFormat>";
</script>

<div class="span2">
    <h2>${'#'}{text['${pojoNameLower}Detail.heading']}</h2>
</div>
<div class="span7">
<h:form id="${pojoNameLower}Form" styleClass="well form-horizontal">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<div class="control-group">
<#if field.equals(pojo.identifierProperty)>
    <#foreach column in field.getColumnIterator()>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
    <h:outputLabel styleClass="control-label" for="${field.name}" value="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    <div class="controls">
        <h:inputText id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}"/>
        <t:message for="${field.name}" styleClass="help-inline"/>
    </div>
    <#else>
        <#lt/>    <h:inputHidden value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}"/>
    </#if>
    </#foreach>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <h:outputLabel styleClass="control-label" for="${field.name}" value="${'#'}{text['${pojoNameLower}.${field.name}']}"/>
    <#foreach column in field.getColumnIterator()>
    <div class="controls">
        <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
            <#lt/>        <h:inputText id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}">
            <f:convertDateTime pattern="${'#'}{text['date.format']}"/>
        </h:inputText>
        <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
            <#lt/>        <h:selectBooleanCheckbox value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}"/>
        <#else>
            <#lt/>        <h:inputText id="${field.name}" value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" required="${(!column.nullable)?string}"<#if (column.length > 0)> maxlength="${column.length?c}"</#if>/>
        </#if>
        <p:message for="${field.name}"/>
    </div>
    </#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
    <div class="controls">
            <#lt/>        <!-- todo: change this to read the identifier field from the other pojo -->
            <#lt/>        <h:selectOneMenu value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}" id="${field.name}" required="${(!column.nullable)?string}" styleClass="select">
            <f:selectItems value="${'#'}{${pojoNameLower}Form.${pojoNameLower}.${field.name}}"/>
        </h:selectOneMenu>
        <p:message for="${field.name}"/>
    </div>
    </#foreach>
</#if>
</div>
</#foreach>
    <div class="form-actions">
        <h:commandButton value="${'#'}{text['button.save']}" action="${'#'}{${pojoNameLower}Form.save}"
            id="save" styleClass="btn btn-primary"/>

        <#-- Todo: hide this button when the id is not assigned. -->
        <#--<c:if test="${'$'}{not empty ${pojoNameLower}Form.${pojoNameLower}.${idFieldName}}">-->
            <h:commandButton value="${'#'}{text['button.delete']}" action="${'#'}{${pojoNameLower}Form.delete}"
                id="delete" styleClass="btn" onclick="return confirmMessage(msgDelConfirm)"/>
        <#--</c:if>-->

        <h:commandButton value="${'#'}{text['button.cancel']}" action="cancel" immediate="true"
            id="cancel" styleClass="btn"/>
    </div>
</h:form>
</div>
</f:view>
</html>
