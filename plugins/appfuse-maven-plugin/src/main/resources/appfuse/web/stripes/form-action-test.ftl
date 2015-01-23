<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import org.junit.After;
import org.junit.Test;

import javax.servlet.Filter;

import static org.junit.Assert.*;

public class ${pojo.shortName}FormBeanTest {
    private MockServletContext servletContext;

    @Test
    public void testView() throws Exception {
        servletContext = new StripesTestFixture().getServletContext();

        MockRoundtrip trip = new MockRoundtrip(servletContext, ${pojo.shortName}FormBean.class);
        String ${pojo.identifierProperty.name} = "-1";
        trip.addParameter("${pojo.identifierProperty.name}", ${pojo.identifierProperty.name});
        trip.execute();

        ${pojo.shortName}FormBean bean = trip.getActionBean(${pojo.shortName}FormBean.class);
        assertNotNull(bean.get${pojo.shortName}());
    }

    @After
    public void onTearDown() {
        // http://www.stripesframework.org/jira/browse/STS-714
        for (Filter filter : servletContext.getFilters()) {
            filter.destroy();
        }
    }
}
