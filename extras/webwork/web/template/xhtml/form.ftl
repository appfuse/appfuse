<#include "/${parameters.templateDir}/xhtml/form-validate.ftl" />
<#include "/${parameters.templateDir}/simple/form.ftl" />
<table <#t/>
<#if parameters.styleClass?exists> class="${parameters.styleClass?html}"<#else> class="detail"</#if><#t/>
>