<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign dateExists = false>
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
    <li style="display: none">
        <s:hidden key="${pojoNameLower}.${field.name}"/>
    </li>
    </#if>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#foreach column in field.getColumnIterator()>
        <#if field.value.typeName == "java.util.Date">
            <#assign dateExists = true>
            <#lt/>    <s:textfield key="${pojoNameLower}.${field.name}" required="${(!column.nullable)?string}" <#if (column.length > 0)>maxlength="${column.length?c}" </#if>cssClass="text" size="11" title="date"/>
        <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
            <#lt/><li>
            <#lt/>    <s:checkbox key="${pojoNameLower}.${field.name}" cssClass="checkbox" theme="simple"/>
            <#lt/>    <!-- For some reason, key prints out the raw value for the label (i.e. true) instead of the i18n key: https://issues.apache.org/struts/browse/WW-1958-->
            <#lt/>    <s:label for="${pojoNameLower}Form_${pojoNameLower}_${field.name}" value="%{getText('${pojoNameLower}.${field.name}')}" cssClass="choice desc" theme="simple"/>
            <#lt/></li>
        <#else>
            <#lt/>    <s:textfield key="${pojoNameLower}.${field.name}" required="${(!column.nullable)?string}" <#if (column.length > 0)>maxlength="${column.length?c}" </#if>cssClass="text medium"/>
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

<#if dateExists><#rt/>
<script type="text/javascript" src="<c:url value='/scripts/calendar/calendar.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendar/calendar-setup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendar/lang/calendar-en.js'/>"></script>
</#if><#rt/>
<script type="text/javascript">
    Form.focusFirstElement($("${pojoNameLower}Form"));
<#foreach field in pojo.getAllPropertiesIterator()>
    <#if !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field) && field.value.typeName == "java.util.Date">
    Calendar.setup({inputField: "${pojoNameLower}Form_${pojoNameLower}_${field.name}", ifFormat: "%m/%d/%Y", button: "${pojoNameLower}.${field.name}DatePicker"});
    </#if>
</#foreach>
</script>
