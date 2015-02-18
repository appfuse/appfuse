
# -- ${pojo.shortName}-START
<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#lt/>${pojoNameLower}.${field.name}=${data.getFieldDescription(field.name)}
</#if>
</#foreach>

${pojoNameLower}.added=${pojo.shortName} has been added successfully.
${pojoNameLower}.updated=${pojo.shortName} has been updated successfully.
${pojoNameLower}.deleted=${pojo.shortName} has been deleted successfully.
${pojoNameLower}.missing=${pojo.shortName} cannot be found with specified id.

# -- ${pojoNameLower} list page --
${pojoNameLower}List.title=${pojo.shortName} List
${pojoNameLower}List.heading=${util.getPluralForWord(pojo.shortName)}
${pojoNameLower}List.${pojoNameLower}=${pojoNameLower}
${pojoNameLower}List.${util.getPluralForWord(pojoNameLower)}=${util.getPluralForWord(pojoNameLower)}
${pojoNameLower}List.message=This is the list of your ${util.getPluralForWord(pojo.shortName)}. You can add ${util.getPluralForWord(pojo.shortName)}. To view a ${pojoNameLower}'s details, edit or delete it, click on one of the ${util.getPluralForWord(pojoNameLower)} from the list.


# -- ${pojoNameLower} detail page --
${pojoNameLower}Detail.title=${pojo.shortName} Detail
${pojoNameLower}Detail.heading=${pojo.shortName} Information
${pojoNameLower}Detail.message=Please enter your ${pojo.shortName} information
# -- ${pojo.shortName}-END