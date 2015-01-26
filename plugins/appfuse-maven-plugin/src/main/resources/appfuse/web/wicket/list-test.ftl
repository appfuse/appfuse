<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.wicket.markup.repeater.data.DataView;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
    <#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
    <#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import org.junit.Test;
import org.springframework.web.context.support.StaticWebApplicationContext;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ${pojo.shortName}ListTest extends BasePageTest {

    @Override
    protected void initSpringBeans(StaticWebApplicationContext context) {
        super.initSpringBeans(context);
        context.getBeanFactory().registerSingleton("${pojoNameLower}Manager", mock(${managerClass}.class));
    }

    @Test
    public void testRenderPage() {
        tester.startPage(${pojo.shortName}List.class);

        // check that the right page was rendered (no unexpected redirect or intercept)
        tester.assertRenderedPage(${pojo.shortName}List.class);
        // assert that there's no error message
        tester.assertNoErrorMessage();
        // check that the right components are in the page
        tester.assertComponent("${util.getPluralForWord(pojoNameLower)}", DataView.class);
    }
}
