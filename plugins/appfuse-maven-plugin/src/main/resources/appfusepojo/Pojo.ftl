${pojo.getPackageDeclaration()}
<#assign classbody>
<#include "PojoTypeDeclaration.ftl"/> {
<#if !pojo.isInterface()>
<#include "PojoFields.ftl"/>
<#include "PojoConstructors.ftl"/>
<#include "PojoPropertyAccessors.ftl"/>
<#include "PojoToString.ftl"/>
<#include "PojoEqualsHashcode.ftl"/>
<#else><#rt/>
<#include "PojoInterfacePropertyAccessors.ftl"/>
</#if><#rt/>
<#include "PojoExtraClassCode.ftl"/><#rt/>
}
</#assign>
${pojo.generateImports()}
${classbody}