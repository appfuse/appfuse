<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}-START-->
                    <t:menuitem name="${pojo.shortName}Menu" title="${pojoNameLower}List.title" page="${pojoNameLower}List"/>
                    <!--${pojo.shortName}-END-->
                    <!-- Add new menu items here -->