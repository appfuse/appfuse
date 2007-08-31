<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
        <!--${pojo.shortName}-START-->
        <Menu name="${pojo.shortName}Menu" title="${pojoNameLower}List.title" page="/${util.getPluralForWord(pojoNameLower)}.html"/>
        <!--${pojo.shortName}-END-->
    </Menus>