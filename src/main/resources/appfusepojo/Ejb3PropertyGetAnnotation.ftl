<#if ejb3>
<#if pojo.hasIdentifierProperty()>
<#if property.equals(clazz.identifierProperty)>
 ${pojo.generateAnnIdGenerator()}
<#-- if this is the id property (getter)-->
<#-- explicitly set the column name for this property-->
</#if>
</#if>
<#if c2h.isManyToOne(property)>
<#--TODO support @OneToOne true and false-->    
@${pojo.importType("javax.persistence.ManyToOne")}(cascade={${pojo.getCascadeType(property)}},
        fetch=${pojo.getFetchType(property)})
    ${pojo.getHibernateCascadeTypeAnnotation(property)}
<#--TODO support optional and targetEntity-->    
${pojo.generateJoinColumnsAnnotation(property)}
<#elseif c2h.isCollection(property)>
${pojo.generateCollectionAnnotation(property, cfg)}
<#else>
${pojo.generateBasicAnnotation(property)}
${pojo.generateAnnColumnAnnotation(property)}
</#if>
</#if>