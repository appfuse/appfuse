<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign numProperties = 0>
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if field.equals(pojo.identifierProperty)>
        <#assign idColumn = column.name>
        <#else><#assign numProperties = numProperties + 1>
        </#if>
    </#foreach>
</#foreach>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN"
    "http://www.ibatis.com/dtd/sql-map-2.dtd">

<sqlMap namespace="${pojo.shortName}SQL">

    <typeAlias alias="${pojoNameLower}" type="${basepackage}.model.${pojo.shortName}"/>

    <parameterMap id="addParam" class="${pojoNameLower}">
    <#foreach field in pojo.getAllPropertiesIterator()><#rt/>
        <#if !field.equals(pojo.identifierProperty) && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)><#rt/>
        <parameter property="${field.name}" jdbcType="${data.getJdbcType(field.value.typeName)}" javaType="${field.value.typeName}"/>
        </#if>
    </#foreach><#rt/>
    </parameterMap>

    <parameterMap id="updateParam" class="${pojoNameLower}">
    <#foreach field in pojo.getAllPropertiesIterator()><#rt/>
        <#if field.equals(pojo.identifierProperty)>
        <parameter property="${idColumn}" jdbcType="${data.getJdbcType(field.value.typeName)}" javaType="${field.value.typeName}"/>
        </#if>
        <#if !field.equals(pojo.identifierProperty) && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
        <parameter property="${field.name}" jdbcType="${data.getJdbcType(field.value.typeName)}" javaType="${field.value.typeName}"/>
        </#if>
    </#foreach><#rt/>
    </parameterMap>

    <resultMap id="${pojoNameLower}Result" class="${pojoNameLower}">
    <#foreach field in pojo.getAllPropertiesIterator()><#rt/>
        <#foreach column in field.getColumnIterator()><#rt/>
            <#assign columnName = column.name>
        </#foreach>
        <#if field.equals(pojo.identifierProperty)>
        <result property="${field.name}" column="${idColumn}"/>
        <#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
        <result property="${field.name}" column="${columnName}"/>
        </#if>
    </#foreach>
    </resultMap>

    <select id="get${util.getPluralForWord(pojo.shortName)}" resultMap="${pojoNameLower}Result">
    <![CDATA[
        select * from ${pojoNameLower}
    ]]>
    </select>

    <select id="get${pojo.shortName}" parameterClass="${pojo.identifierProperty.value.typeName}" resultMap="${pojoNameLower}Result">
    <![CDATA[
        select * from ${pojoNameLower} where ${idColumn} = #value#
    ]]>
    </select>

    <insert id="add${pojo.shortName}" parameterClass="${pojoNameLower}">
        <selectKey resultClass="${pojo.identifierProperty.value.typeName}" keyProperty="${idColumn}" type="post">
            select last_insert_id() as ${idColumn}
        </selectKey>
        <![CDATA[
            insert into ${pojoNameLower} (<#rt/>
            <#foreach field in pojo.getAllPropertiesIterator()>
                <#list field.getColumnIterator() as column><#rt/>
                    <#assign columnName = column.name>
                </#list>
                <#lt/><#if !field.equals(pojo.identifierProperty) && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
                <#lt/>${columnName}<#if field_index lt numProperties>, </#if></#if><#rt/>
            <#lt/></#foreach>)
            values (<#rt/>
            <#foreach field in pojo.getAllPropertiesIterator()>
                <#lt/><#if !field.equals(pojo.identifierProperty) && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
                <#lt/>#${field.name}#<#if field_index lt numProperties>, </#if></#if><#rt/>
            <#lt/></#foreach>)
        ]]>
    </insert>

    <update id="update${pojo.shortName}" parameterClass="${pojoNameLower}">
    <![CDATA[
        update ${pojoNameLower} set 
            <#foreach field in pojo.getAllPropertiesIterator()>
                <#list field.getColumnIterator() as column>
                    <#assign columnName = column.name>
                </#list>
                <#if !field.equals(pojo.identifierProperty) && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
                    <#lt/>            ${columnName} = #${field.name}#<#if field_index lt numProperties>, </#if>
                </#if>
            </#foreach>
        where ${idColumn} = #${idColumn}#
    ]]>
    </update>

    <delete id="delete${pojo.shortName}" parameterClass="${pojo.identifierProperty.value.typeName}">
    <![CDATA[
        delete from ${pojoNameLower} where ${idColumn} = #value#
    ]]>
    </delete>
</sqlMap>
