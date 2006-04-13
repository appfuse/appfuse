package org.appfuse.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.spring.AbstractXFireSpringTest;
import org.jdom.Document;
import org.jmock.Mock;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.stub.ReturnStub;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserWebServiceTest extends AbstractXFireSpringTest {

    protected final Log log = LogFactory.getLog(getClass());

    public void setUp() throws Exception {
        super.setUp();
    }
    
    public void testGetWsdl() throws Exception {
        Document doc = getWSDLDocument("UserWebService");
        printNode(doc);
        
        assertValid("//xsd:complexType[@name=\"User\"]", doc);
        assertValid("//xsd:complexType[@name=\"Role\"]", doc);
    }
    
    public void testGetUser() throws Exception {
        // Setup testharness 
        User testData = new User("tomcat");
        testData.setEnabled(true);
        Mock userDao = new Mock(UserDao.class);
        
        // because we can't extend MockObjectTestCase we create new instances for once(), eq() and returnValue()
        InvokeOnceMatcher once = new InvokeOnceMatcher();
        IsEqual eq = new IsEqual("tomcat");
        ReturnStub returnValue = new ReturnStub(testData);
        userDao.expects(once).method("getUser").with(eq).will(returnValue);
        
        UserManager service = (UserManager) getContext().getBean("userManager");
        service.setUserDao((UserDao)userDao.proxy());
        
        // invoke webservice
        Document response =
            invokeService("UserWebService", "/org/appfuse/service/getUser.xml");

        //printNode(response);
        // verify result
        userDao.verify();
        addNamespace("service","http://service.appfuse.org");
        addNamespace("model","http://model.appfuse.org");
        assertValid("//service:getUserResponse/service:out[model:username=\"tomcat\"]",response);
        assertValid("//service:getUserResponse/service:out[model:enabled=\"true\"]",response);
    }
    
    public void testGetUsers() throws Exception {
        Service service = getServiceRegistry().getService("UserWebService");
        UserWebService client = (UserWebService)
            new XFireProxyFactory(getXFire()).create(service, "xfire.local://UserWebService");

        final User testUser = new User("tomcat");
        User user = new User();

        List testData = new ArrayList(){{ add(testUser); }};
        testUser.setEnabled(true);
        Mock userDAO = new Mock(UserDAO.class);
        
        // because we can't extend MockObjectTestCase we create new instances for once(), eq() and returnValue()
        InvokeOnceMatcher once = new InvokeOnceMatcher();
        ReturnStub returnValue = new ReturnStub(testData);
        IsEqual eq = new IsEqual(user);
        userDAO.expects(once).method("getUsers").with(eq).will(returnValue);
        
        UserManager userService = (UserManager) getContext().getBean("userManager");
        userService.setUserDAO((UserDAO)userDAO.proxy());

        List userList = (List)client.getUsers(user);
        assertNotNull(userList);
        userDAO.verify();
    }
    
    protected ApplicationContext createContext() {
        return new ClassPathXmlApplicationContext(new String[]{
                "org/appfuse/service/applicationContext-test.xml",
                "org/appfuse/service/applicationContext-webservice.xml"});
    }
    

}
