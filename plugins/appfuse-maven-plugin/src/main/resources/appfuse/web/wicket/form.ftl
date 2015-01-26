<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};

import java.util.Date;

@MountPath("${pojoNameLower}form")
public class ${pojo.shortName}Form extends AbstractWebPage {
    @SpringBean(name = "${pojoNameLower}Manager")
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
    private final Page responsePage;

    public ${pojo.shortName}Form() {
        this(new ${pojo.shortName}List(), new ${pojo.shortName}());
    }

    /**
     * Constructor used to edit an ${pojoNameLower}
     *
     * @param responsePage page to navigate to after this page completes its work
     * @param ${pojoNameLower} ${pojoNameLower} to edit
    */
    public ${pojo.shortName}Form(final Page responsePage, ${pojo.shortName} ${pojoNameLower}) {
        this.responsePage = responsePage;

        // Add feedback panel for error messages
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        feedbackPanel.setVisible(false);

        // Create and add the form
        EditForm form = new EditForm("${pojoNameLower}-form", ${pojoNameLower}) {
            protected void onSave(${pojo.shortName} ${pojoNameLower}) {
                onSave${pojo.shortName}(${pojoNameLower});
            }

            protected void onCancel() {
                onCancelEditing();
            }

            protected void onDelete(${pojo.shortName} ${pojoNameLower}) {
                onDelete${pojo.shortName}(${pojoNameLower});
            }
        };
        add(form);
    }

    /**
     * Listener method for save action
     *
     * @param ${pojoNameLower} ${pojoNameLower} bean
     */
    protected void onSave${pojo.shortName}(${pojo.shortName} ${pojoNameLower}) {
        boolean isNew = false;
        if (${pojoNameLower}.${getIdMethodName}() == null) {
            ${pojoNameLower}.setVersion(null);
            isNew = true;
        }

        try {
            ${pojoNameLower}Manager.save(${pojoNameLower});
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error saving ${pojoNameLower}: " + e.getMessage());
        }

        String message = getLocalizer().getString("${pojoNameLower}.updated", this);
        if (isNew) {
            message = getLocalizer().getString("${pojoNameLower}.added", this);
        }
        getSession().info(message);
        if (isNew) {
            FeedbackPanel feedback = (FeedbackPanel) responsePage.get("feedback");
            feedback.setVisible(true);
            feedback.setEscapeModelStrings(true);
            throw new RestartResponseAtInterceptPageException(responsePage);
        } else {
            FeedbackPanel feedback = (FeedbackPanel) this.get("feedback");
            feedback.setVisible(true);
            feedback.setEscapeModelStrings(true);
        }
    }

    /**
     * Listener method for delete action
     *
     * @param ${pojoNameLower} ${pojoNameLower} bean
     */
    protected void onDelete${pojo.shortName}(${pojo.shortName} ${pojoNameLower}) {
        ${pojoNameLower}Manager.remove(${pojoNameLower}.${getIdMethodName}());

        String message = getLocalizer().getString("${pojoNameLower}.deleted", this);
        getSession().info(message);

        responsePage.get("feedback").setVisible(true);
        // how to redirect in Wicket 6: http://stackoverflow.com/a/23960578/65681
        throw new RestartResponseAtInterceptPageException(responsePage);
    }

    /**
     * Lister method for cancel action
     */
    private void onCancelEditing() {
        setResponsePage(responsePage);
    }

    /**
     * Subclass of Form used to edit an ${pojoNameLower}
     */
    private static abstract class EditForm extends Form {
        /**
         * Convenience method that adds and prepares a form component
         *
         * @param fc    form component
         * @param label IModel containing the string used in ${'$'}{label} variable of
         *              validation messages
         */
        private void add(FormComponent fc, IModel<String> label) {
            // Add the component to the form
            super.add(fc);
            // Set its label model
            fc.setLabel(label);
            // Add feedback panel that will be used to display component errors
            add(new ComponentFeedbackPanel(fc.getId() + "-feedback", fc));
        }

        /**
         * Constructor
         *
         * @param ${pojo.identifierProperty.name} component ${pojo.identifierProperty.name}
         * @param ${pojoNameLower} ${pojo.shortName} object that will be used as a form bean
         */
        public EditForm(String ${pojo.identifierProperty.name}, ${pojo.shortName} ${pojoNameLower}) {
            /*
             * Wrap the ${pojoNameLower} bean with a CompoundPropertyModel, this allows
             * us to easily connect form components to the bean properties
             * (component ${pojo.identifierProperty.name} is used as the property expression)
             */
            super(${pojo.identifierProperty.name}, new CompoundPropertyModel<>(${pojoNameLower}));
<#foreach field in pojo.getAllPropertiesIterator()>
    <#assign isDate = false>
    <#assign isBoolean = false>
    <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
        <#assign isDate = true>
    </#if>
    <#if field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
        <#assign isBoolean = true>
    </#if>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#if isDate><#rt/>
                <#lt/>            DateTextField ${field.name} = new DateTextField("${field.name}");
                <#lt/>            ${field.name}.setRequired(true);
                <#lt/>            add(${field.name}, new ResourceModel("${pojoNameLower}.${field.name}"));
            <#elseif isBoolean><#rt/>
                <#lt/>            add(new CheckBox("${field.name}"), new ResourceModel("${pojoNameLower}.${field.name}"));
            <#else><#rt/>
                <#lt/>            add(new RequiredTextField<>("${field.name}"), new ResourceModel("${pojoNameLower}.${field.name}"));
            </#if>
        <#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#if isDate><#rt/>
                <#lt/>            add(new DateTextField<>("${field.name}"), new ResourceModel("${pojoNameLower}.${field.name}")));
            <#elseif isBoolean><#rt/>
                <#lt/>            add(new CheckBox("${field.name}"), new ResourceModel("${pojoNameLower}.${field.name}")));
            <#elseif !field.equals(pojo.identifierProperty)><#rt/>
                <#lt/>            add(new TextField<>("${field.name}"), new ResourceModel("${pojoNameLower}.${field.name}"));
            </#if>
        </#if>
    </#foreach>
</#foreach>

            add(new Button("save", new StringResourceModel("button.save", this, null)) {
                public void onSubmit() {
                    onSave((${pojo.shortName}) getForm().getModelObject());
                }
            });

            Button delete = new Button("delete", new StringResourceModel("button.delete", this, null)) {
                public void onSubmit() {
                    onDelete((${pojo.shortName}) getForm().getModelObject());
                }
            };

            if (${pojoNameLower}.${getIdMethodName}() == null) {
                delete.setVisible(false);
                delete.setEnabled(false);
            }
            add(delete);

            /*
             * Notice the setDefaultFormProcessing(false) call at the end. This
             * tells wicket that when this button is pressed it should not
             * perform any form processing (ie bind request values to the bean).
            */
            add(new Button("cancel", new StringResourceModel("button.cancel", this, null)) {
                    public void onSubmit() {
                        onCancel();
                    }
                }.setDefaultFormProcessing(false));
        }

        /**
         * Callback for cancel button
         */
        protected abstract void onCancel();

        /**
         * Callback for delete button
         *
         * @param ${pojoNameLower} ${pojoNameLower} bean
         */
        protected abstract void onDelete(${pojo.shortName} ${pojoNameLower});

        /**
         * Callback for save button
         *
         * @param ${pojoNameLower} ${pojoNameLower} bean
         */
        protected abstract void onSave(${pojo.shortName} ${pojoNameLower});
    }
}
