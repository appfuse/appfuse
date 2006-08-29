<#if parameters.labelposition?default("top") == 'bottom'>
        <p><label <#t/>
    <#if parameters.id?exists>
            for="${parameters.id?html}" <#t/>
    </#if>
    <#if hasFieldErrors>
            class="error"<#t/>
    </#if>
    ><#t/>
        ${parameters.label?html}
    <#if parameters.required?default(false)>
            <span class="req">*</span><#t/>
    </#if>
    <#include "/${parameters.templateDir}/xhtml/tooltip.ftl" />
    </label></p><#t/>
    <#if parameters.labelposition?default("top") == 'top'>
    </div> <#rt/>
    <#else>
    </span> <#rt/>
    </#if>
    <#if hasFieldErrors>
    <#list fieldErrors[parameters.name] as error>
        <span class="fieldError"><img src="./images/iconWarning.gif" alt="Validation Error" class="icon" /> ${error?html}</span><#lt/>
    </#list>
    </#if>
</#if>
${parameters.after?if_exists}<#t/>
    <#lt/>
<#if parameters.labelposition?default("top") == 'top'>
</div> <#rt/>
</#if>
</li>