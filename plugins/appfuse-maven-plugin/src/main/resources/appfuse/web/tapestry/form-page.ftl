<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE page-specification PUBLIC
    "-//Apache Software Foundation//Tapestry Specification 4.0//EN"
    "http://jakarta.apache.org/tapestry/dtd/Tapestry_4_0.dtd">

<page-specification>
    <inject property="engineService" object="engine-service:page"/>
    <inject property="request" object="service:tapestry.globals.HttpServletRequest"/>
    <inject property="response" object="service:tapestry.globals.HttpServletResponse"/>
    <inject property="${pojoNameLower}Manager" type="spring" object="${pojoNameLower}Manager"/>

    <property name="message" persist="flash"/>

    <component id="${pojoNameLower}Form" type="Form">
        <binding name="delegate" value="ognl:delegate"/>
        <binding name="clientValidationEnabled" value="true"/>
    </component>

<#foreach field in pojo.getAllPropertiesIterator()>
<#if !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#foreach column in field.getColumnIterator()>
    <#if field.value.typeName == "java.lang.String">

    <component id="${field.name}Field" type="TextField">
        <binding name="value" value="${pojoNameLower}.${field.name}"/>
        <#if !column.nullable>
            <#lt/>        <binding name="validators" value="validators:required"/>
        </#if>
        <binding name="displayName" value="message:${pojoNameLower}.${field.name}"/>
    </component>
    </#if>
    </#foreach>
</#if>
</#foreach>
</page-specification>
