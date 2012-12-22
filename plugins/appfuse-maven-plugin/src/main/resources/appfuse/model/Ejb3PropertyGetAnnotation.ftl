<#if ejb3><#rt/>
    <#if pojo.hasIdentifierProperty()><#rt/>
        <#if property.equals(clazz.identifierProperty)><#rt/>
        <#lt/>${pojo.generateAnnIdGenerator()} @DocumentId<#rt/>
        </#if><#rt/>
    </#if><#rt/>
    <#if c2h.isManyToOne(property)><#rt/>
        <#--TODO support @OneToOne true and false-->
        <#lt/>${pojo.generateManyToOneAnnotation(property)}
        <#--TODO support optional and targetEntity-->
        <#lt/>${pojo.generateJoinColumnsAnnotation(property)}
        <#elseif c2h.isCollection(property)><#rt/>
            <#lt/>${pojo.generateCollectionAnnotation(property, cfg)}
        <#else><#rt/>
        <#lt/>${pojo.generateBasicAnnotation(property)}
        <#if !property.equals(clazz.identifierProperty)><#rt/>
        <#lt/>${pojo.generateAnnColumnAnnotation(property)}
        <#lt/>    @Field
        </#if><#rt/>
    </#if><#rt/>
</#if><#rt/>