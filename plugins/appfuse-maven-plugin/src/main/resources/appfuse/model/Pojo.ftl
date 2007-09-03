${pojo.getPackageDeclaration()}

<#assign classbody>
<#include "PojoTypeDeclaration.ftl"/> {
<#if !pojo.isInterface()>
<#include "PojoFields.ftl"/>
<#--include "PojoConstructors.ftl"/-->

<#include "PojoPropertyAccessors.ftl"/>

<#include "PojoEqualsHashcode.ftl"/>

<#include "PojoToString.ftl"/>

<#else>
<#include "PojoInterfacePropertyAccessors.ftl"/>

</#if>
<#include "PojoExtraClassCode.ftl"/>

}
</#assign>
import ${appfusepackage}.model.BaseObject;

${pojo.generateImports()}
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.io.Serializable;

${classbody}