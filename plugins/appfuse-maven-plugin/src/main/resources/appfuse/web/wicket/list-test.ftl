<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
    <#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
    <#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ${pojo.shortName}ListTest {
    private WicketTester tester;

    @Before
    public void onSetUp() {
        tester = new WicketTester();
        ApplicationContextMock applicationContextMock = new ApplicationContextMock();
        applicationContextMock.putBean("${pojoNameLower}Manager", mock(${managerClass}.class));
        tester.getApplication().getComponentInstantiationListeners().add(
            new SpringComponentInjector(tester.getApplication(), applicationContextMock));
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
