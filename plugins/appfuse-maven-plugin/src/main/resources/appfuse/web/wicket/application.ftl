<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
// -- ${pojo.shortName}-START
        mountPage("/${util.getPluralForWord(pojoNameLower)}", ${basepackage}.webapp.pages.${pojo.shortName}List.class);
        mountPage("/${pojoNameLower}form", ${basepackage}.webapp.pages.${pojo.shortName}Form.class);
        // -- ${pojo.shortName}-END
        // add additional pages here
