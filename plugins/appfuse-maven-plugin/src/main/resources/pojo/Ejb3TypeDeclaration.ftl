<#if ejb3?if_exists><#rt/>
<#if pojo.isComponent()><#rt/>
@${pojo.importType("javax.persistence.Embeddable")}
<#else><#rt/>
@${pojo.importType("javax.persistence.Entity")}
@${pojo.importType("javax.persistence.Table")}(name="${clazz.table.name}"<#rt/>
<#if clazz.table.schema?exists><#rt/>
    <#lt>,schema="${clazz.table.schema}"<#rt/>
</#if><#if clazz.table.catalog?exists><#rt/>
    <#lt>,catalog="${clazz.table.catalog}"<#rt/>
</#if><#rt/>
<#assign uniqueConstraint=pojo.generateAnnTableUniqueConstraint()><#rt/>
<#if uniqueConstraint?has_content><#rt/>
    <#lt/>, uniqueConstraints = ${uniqueConstraint} 
</#if>)
</#if>
</#if>