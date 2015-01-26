<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
    <#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
    <#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import ${pojo.packageName}.${pojo.shortName};
import ${appfusepackage}.util.DateUtil;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.context.support.StaticWebApplicationContext;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ${pojo.shortName}FormTest extends BasePageTest {

<#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>

    @Override
    protected void initSpringBeans(StaticWebApplicationContext context) {
        super.initSpringBeans(context);
        ${pojoNameLower}Manager = mock(${managerClass}.class);
        context.getBeanFactory().registerSingleton("${pojoNameLower}Manager", ${pojoNameLower}Manager);
        ${pojo.shortName}Form ${pojoNameLower}Form = new ${pojo.shortName}Form();
        tester.startPage(${pojoNameLower}Form);
    }

    @Test
    public void shouldDisplayErrorMessagesOnAddEmpty${pojo.shortName}() {
        // given
        tester.assertRenderedPage(${pojo.shortName}Form.class);
        tester.assertNoErrorMessage();
        tester.assertComponent("${pojoNameLower}-form", Form.class);

        // when
        tester.submitForm("${pojoNameLower}-form");

        // then
        tester.assertErrorMessages(new String[] {
<#foreach field in pojo.getAllPropertiesIterator()>
    <#assign isBoolean = false>
    <#if field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
        <#assign isBoolean = true>
    </#if>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !isBoolean && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            "'${data.getFieldDescription(field.name)}' is required.",
        </#if>
    </#foreach>
</#foreach>
        });
    }

    @Test
    public void shouldSave${pojo.shortName}AndRedirectTo${pojo.shortName}ListOnSubmit${pojo.shortName}WithRequiredData() {
        // given
        tester.assertRenderedPage(${pojo.shortName}Form.class);
        tester.assertNoErrorMessage();
        FormTester formTester = tester.newFormTester("${pojoNameLower}-form");
        ${pojo.shortName} ${pojoNameLower} = createTest${pojo.shortName}();

        // when
        fillFormWithValuesFrom${pojo.shortName}(formTester, ${pojoNameLower});
        formTester.submit("save");

        // then
        verify(${pojoNameLower}Manager).save(${pojoNameLower});
        tester.assertRenderedPage(${pojo.shortName}List.class);
        tester.assertNoErrorMessage();
        tester.assertInfoMessages("${pojo.shortName} has been added successfully.");
    }

    private ${pojo.shortName} createTest${pojo.shortName}() {
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>
        return ${pojoNameLower};
    }

    private void fillFormWithValuesFrom${pojo.shortName}(FormTester formTester, ${pojo.shortName} ${pojoNameLower}) {
<#foreach field in pojo.getAllPropertiesIterator()>
    <#assign isDate = false>
    <#assign isBoolean = false>
    <#assign isNumber = false>
    <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
        <#assign isDate = true>
    </#if>
    <#if field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
        <#assign isBoolean = true>
    </#if>
    <#if field.value.typeName == "int" || field.value.typeName == "java.lang.Integer" || field.value.typeName == "java.lang.Long" || field.value.typeName == "long">
        <#assign isNumber = true>
    </#if>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#if isBoolean>
                <#lt>        formTester.setValue("${field.name}", ${pojoNameLower}.is${pojo.getPropertyName(field)}());
            <#elseif isDate>
                <#lt>        formTester.setValue("${field.name}", DateUtil.convertDateToString(${pojoNameLower}.get${pojo.getPropertyName(field)}()));
            <#elseif isNumber>
                <#lt>        formTester.setValue("${field.name}", String.valueOf(${pojoNameLower}.get${pojo.getPropertyName(field)}()));
            <#else>
                <#lt>        formTester.setValue("${field.name}", ${pojoNameLower}.get${pojo.getPropertyName(field)}());
            </#if>
        </#if>
    </#foreach>
</#foreach>
    }
}
