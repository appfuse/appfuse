<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import java.util.List;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("${util.getPluralForWord(pojoNameLower)}")
public class ${pojo.shortName}List extends AbstractWebPage {
    @SpringBean(name = "${pojoNameLower}Manager")
    <#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
    <#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
    </#if>

    public ${pojo.shortName}List() {
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        feedbackPanel.setVisible(false); // other pages will set this to visible
        feedbackPanel.setEscapeModelStrings(false);

        // Form and button for routing ${pojoNameLower} to add a new ${pojoNameLower}
        Form form = new Form("form");
        Button button = new Button("add-${pojoNameLower}") {
            public void onSubmit() {
                onEdit${pojo.shortName}(new ${pojo.shortName}());
            }
        };
        button.setDefaultFormProcessing(false);
        form.add(button);
        add(form);

        Sortable${pojo.shortName}DataProvider dp = new Sortable${pojo.shortName}DataProvider(${pojoNameLower}Manager);

        final DataView<${pojo.shortName}> dataView = new DataView<${pojo.shortName}>("${util.getPluralForWord(pojoNameLower)}", dp) {
            protected void populateItem(final Item<${pojo.shortName}> item) {
                ${pojo.shortName} ${pojoNameLower} = item.getModelObject();

                Link<${pojo.shortName}> link = new Link<${pojo.shortName}>("edit-link", item.getModel()) {
                    public void onClick() {
                        onEdit${pojo.shortName}(getModelObject());
                    }
                };

                link.add(new Label("${pojoNameLower}.${pojo.identifierProperty.name}", ${pojoNameLower}.${getIdMethodName}()));
                item.add(link);
        <#foreach field in pojo.getAllPropertiesIterator()>
            <#assign isDate = false>
            <#assign isBoolean = false>
            <#if field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
                <#assign isBoolean = true>
            </#if>
            <#if !field.equals(pojo.identifierProperty)>
                item.add(new Label("${pojoNameLower}.${field.name}", ${pojoNameLower}.<#if isBoolean>is<#else>get</#if>${pojo.getPropertyName(field)}()));
            </#if>
        </#foreach>
                item.add(new AttributeModifier("class", new LoadableDetachableModel() {
                    protected Object load() {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
            }
        };

        dataView.setItemsPerPage(10);

    <#foreach field in pojo.getAllPropertiesIterator()>
        add(new OrderByBorder<>("orderBy${pojo.getPropertyName(field)}", "${field.name}", dp));
    </#foreach>
        add(dataView);

        add(new PagingNavigator("navigator", dataView));
    }

    /**
     * Listener for the edit action
     *
     * @param ${pojoNameLower} ${pojoNameLower} to be edited
     */
    protected void onEdit${pojo.shortName}(${pojo.shortName} ${pojoNameLower}) {
        setResponsePage(new ${pojo.shortName}Form(this, ${pojoNameLower}));
    }
}
