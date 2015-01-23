<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.model.${pojo.shortName};
import org.springframework.orm.ObjectRetrievalFailureException;

@UrlBinding("/${pojoNameLower}form.action")
public class ${pojo.shortName}FormBean extends BaseActionBean {
    @SpringBean
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
    private ${pojo.shortName} ${pojoNameLower};
    private ${identifierType} ${pojo.identifierProperty.name};

    public void ${setIdMethodName}(${identifierType} ${pojo.identifierProperty.name}) {
        this.${pojo.identifierProperty.name} = ${pojo.identifierProperty.name};
    }

    public ${pojo.shortName} get${pojo.shortName}() {
        return ${pojoNameLower};
    }

    @ValidateNestedProperties({
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>            @Validate(field = "${field.name}", required = true),
        </#if>
    </#foreach>
</#foreach>
    })
    public void set${pojo.shortName}(${pojo.shortName} ${pojoNameLower}) {
        this.${pojoNameLower} = ${pojoNameLower};
    }

    @DontValidate @DefaultHandler
    public Resolution view() {
        if (${pojo.identifierProperty.name} != null) {
            try {
                ${pojoNameLower} = ${pojoNameLower}Manager.get(${pojo.identifierProperty.name});
            } catch (ObjectRetrievalFailureException e) {
                e.printStackTrace();
                getContext().getMessages().add(new LocalizableMessage("${pojoNameLower}.missing"));
                return showList();
            }
        } else {
            ${pojoNameLower} = new ${pojo.shortName}();
        }
        return showForm();
    }

    @HandlesEvent("save")
    public Resolution save() {
        ${pojoNameLower}Manager.save(${pojoNameLower});
        if (${pojoNameLower}.${getIdMethodName}() == null) {
            getContext().getMessages().add(new LocalizableMessage("${pojoNameLower}.added"));
        } else {
            getContext().getMessages().add(new LocalizableMessage("${pojoNameLower}.updated"));
            return showForm();
        }
        return showList();
    }

    @DontValidate
    @HandlesEvent("delete")
    public Resolution delete() {
        ${pojoNameLower}Manager.remove(${pojoNameLower}.${getIdMethodName}());
        getContext().getMessages().add(new LocalizableMessage("${pojoNameLower}.deleted"));
        return showList();
    }

    @DontValidate
    @HandlesEvent("cancel")
    public Resolution cancel() {
        return showList();
    }

    private Resolution showList() {
        return new RedirectResolution("/${util.getPluralForWord(pojoNameLower)}").flash(this);
    }

    private Resolution showForm() {
        return new ForwardResolution("/${pojoNameLower}Form.jsp");
    }
}
