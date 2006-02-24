<#--
	if the label position is top,
	then give the label it's own row in the table
-->
<#assign hasFieldErrors = parameters.name?exists && fieldErrors?exists && fieldErrors[parameters.name]?exists/>
<tr>
<#if parameters.labelposition?default("") == 'top'>
    <th colspan="2"><#rt/>
<#else>
    <th><#rt/>
</#if>
<#if parameters.label?exists>
    <label <#t/>
<#if parameters.id?exists>
        for="${parameters.id?html}" <#t/>
</#if>
<#if hasFieldErrors>
        class="error"><#t/>
<#elseif parameters.required?default(false)>
        class="required">* <#t/>
<#else>
        ><#t/>
</#if>
${parameters.label?html}:<#t/>
<#include "/${parameters.templateDir}/xhtml/tooltip.ftl" /> 
</label><#t/>
</#if>
<#if hasFieldErrors>
<a class="errorLink" href="?" onclick="showHelpTip(event, '<#list fieldErrors[parameters.name] as error>${error?html}<br /></#list>', false); return false" onmouseover="showHelpTip(event, '<#list fieldErrors[parameters.name] as error>${error?html}<br /></#list>', false); return false" onmouseout="hideHelpTip(event); return false"><img src="./images/iconWarning.gif" alt="Validation Error" class="icon" />
</#if>
    </th><#lt/>
<#-- add the extra row -->
<#if parameters.labelposition?default("") == 'top'>
</tr>
<tr>
</#if>
