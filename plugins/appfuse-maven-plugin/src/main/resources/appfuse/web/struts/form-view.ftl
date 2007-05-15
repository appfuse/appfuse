<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="${pojoNameLower}Detail.title"/></title>
    <meta name="heading" content="<fmt:message key='${pojoNameLower}Detail.heading'/>"/>
</head>

<s:form id="${pojoNameLower}Form" action="save${pojo.shortName}" method="post" validate="true">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if field.equals(pojo.identifierProperty)>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
        <#lt/>    <s:textfield key="${pojoNameLower}.${field.name}" required="true" cssClass="text medium"/>
    <#else>
        <#lt/>    <s:hidden key="${pojoNameLower}.${field.name}" cssClass="text medium"/>
    </#if>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
        <#if field.value.typeName == "java.util.Date">
            <#lt/>    <s:textfield key="${pojoNameLower}.${field.name}" required="${(!column.nullable)?string}" cssClass="text medium"/> <!-- todo: add calendar -->
        <#elseif field.value.typeName == "boolean">
            <#lt/>    <s:checkbox key="${pojoNameLower}.${field.name}" required="${(!column.nullable)?string}" cssClass="choice"/>
        <#else>
            <#lt/>    <s:textfield key="${pojoNameLower}.${field.name}" required="${(!column.nullable)?string}" cssClass="text medium"/>
        </#if>
    </#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
            <#lt/>    <!-- todo: change this to read the identifier field from the other pojo -->
            <#lt/>    <s:select name="${pojoNameLower}.${field.name}.id" list="${field.name}List" listKey="id" listValue="id"></s:select>
    </#foreach>
</#if>
</#foreach>

    <li class="buttonBar bottom">
        <s:submit cssClass="button" method="save" key="button.save" theme="simple"/>
        <c:if test="${'$'}{not empty ${pojoNameLower}.${idFieldName}}">
            <s:submit cssClass="button" method="delete" key="button.delete"
                onclick="return confirmDelete('${pojo.shortName}')" theme="simple"/>
        </c:if>
        <s:submit cssClass="button" method="cancel" key="button.cancel" theme="simple"/>
    </li>
</s:form>

<script type="text/javascript">
    Form.focusFirstElement($("${pojoNameLower}Form"));
</script>
