<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}-START-->
                    <li><a href="${'$'}{rc.contextPath}/${util.getPluralForWord(pojoNameLower)}" title="View ${util.getPluralForWord(pojo.shortName)}"><span>${util.getPluralForWord(pojo.shortName)}</span></a></li>
                    <!--${pojo.shortName}-END-->
                    <!-- Add new menu items here -->
