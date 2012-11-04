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

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

${pojo.generateImports()}
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

${classbody}