package org.appfuse.webapp.action;


public class PasswordHintActionTest extends BaseStrutsTestCase {

    public PasswordHintActionTest(String name) {
        super(name);
    }

    public void testExecute() throws Exception {
        setRequestPathInfo("/passwordHint");

        addRequestParameter("username", "tomcat");
        actionPerform();

        verifyForward("previousPage");
        verifyNoActionErrors();
    }
}
