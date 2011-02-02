<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign pageName = util.getPluralForWord(pojoNameLower) + "">
<#if webframework == "tapestry"><#assign pageName = pojoNameLower + "List"></#if>
        <!--${pojo.shortName}-START-->
        <Menu name="${pojo.shortName}Menu" title="${pojoNameLower}List.title" page="/${pageName}"/>
        <!--${pojo.shortName}-END-->
    </Menus>
