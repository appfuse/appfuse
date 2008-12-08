package org.appfuse.webapp.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.webapp.services.AppModule;
import org.appfuse.webapp.services.TestModule;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public abstract class BasePageTester extends Assert {
	
	protected PageTester tester;
	protected Document doc;
	protected Map<String, String> fieldValues;
    
//	@BeforeMethod
	@BeforeTest(alwaysRun=true) 
	protected void before() {
		String appPackage = "org.appfuse.webapp";
		String appName = "app"; 
		tester = new PageTester(appPackage, appName, "src/main/webapp", SpringModuleDef.class);
		//tester = new PageTester(appPackage, "test", "src/main/webapp");
		fieldValues = new HashMap<String, String>();
	}

	//@AfterMethod
	@AfterTest(alwaysRun=true) 
	protected void after() {
		if (tester != null) {
			tester.shutdown();
		}

	}

    
}