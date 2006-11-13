<#foreach field in pojo.getAllPropertiesIterator()>
    <#if pojo.getMetaAttribAsBool(field, "gen-property", true)> 
        <#if pojo.hasMetaAttribute(field, "field-description")>    
            /**${pojo.getFieldJavaDoc(field, 0)} */
        </#if>
        /** DOCUMENT ME! */
        ${pojo.getFieldModifiers(field)} ${pojo.getJavaTypeName(field, jdk5)} ${field.name}
        <#if pojo.hasFieldInitializor(field, jdk5)>
            = ${pojo.getFieldInitialization(field, jdk5)}
        </#if>;
    </#if>
</#foreach>
