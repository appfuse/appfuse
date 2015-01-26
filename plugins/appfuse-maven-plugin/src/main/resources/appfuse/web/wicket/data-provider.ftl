<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
    <#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
    <#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import ${pojo.packageName}.${pojo.shortName};

public class Sortable${pojo.shortName}DataProvider extends SortableDataProvider<${pojo.shortName}, String> {
    <#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
    <#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
    </#if>

    public Sortable${pojo.shortName}DataProvider(${managerClass} ${pojoNameLower}Manager) {
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    public Sortable${pojo.shortName}DataProvider() {
        // default sort
<#foreach field in pojo.getAllPropertiesIterator()>
    <#if field_index == 1>
        setSort("${field.name}", SortOrder.ASCENDING);
    </#if>
</#foreach>
    }

    @SuppressWarnings("unchecked")
    public Iterator iterator(long first, long count) {
        List ${util.getPluralForWord(pojoNameLower)} = ${pojoNameLower}Manager.getAll();
        if (first > 0) {
            ${util.getPluralForWord(pojoNameLower)} = ${util.getPluralForWord(pojoNameLower)}.subList((int) first, (int) (first + count));
        }

        SortParam sp = getSort();

        if (sp != null) {
            Object sortColumn = sp.getProperty();
            Comparator comparator = new BeanComparator(sortColumn.toString());

            if (!sp.isAscending()) {
                comparator = new ReverseComparator(comparator);
            }

            Collections.sort(${util.getPluralForWord(pojoNameLower)}, comparator);
        }

        return ${util.getPluralForWord(pojoNameLower)}.iterator();
    }

    public long size() {
        return ${pojoNameLower}Manager.getAll().size();
    }

    @Override
    public IModel<${pojo.shortName}> model(${pojo.shortName} ${pojoNameLower}) {
        return new ${pojo.shortName}Model(${pojoNameLower}, ${pojoNameLower}Manager);
    }
}
