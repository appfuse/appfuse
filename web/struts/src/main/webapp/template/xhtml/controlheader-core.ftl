<#--
    if the label position is top,
    then give the label it's own row in the table
-->
<#assign hasFieldErrors = parameters.name?exists && fieldErrors?exists && fieldErrors[parameters.name]?exists/>
<#if parameters.label?exists && parameters.labelposition?default("top") == 'top'>
    <label <#t/>
    <#if parameters.id?exists>
        for="${parameters.id?html}" <#t/>
    </#if>
    <#if hasFieldErrors>
        class="desc error"><#t/>
    <#else>
        class="desc"><#t/>
    </#if>
${parameters.label?html}<#t/>
<#include "/${parameters.templateDir}/xhtml/tooltip.ftl" /> 
<#if parameters.required?default(false)> <span class="req">*</span></#if></label><#t/>
</#if>
<#if hasFieldErrors && parameters.labelposition?default("top") != 'bottom'>
<#list fieldErrors[parameters.name] as error>
    <span class="fieldError"><img src="${base}/images/iconWarning.gif" alt="Validation Error" class="icon" /> ${error?html}</span><#lt/>
</#list>
</#if>
