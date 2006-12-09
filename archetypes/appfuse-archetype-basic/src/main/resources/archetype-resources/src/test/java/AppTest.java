package $package;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {
  public void testGetHello() throws Exception {
	assertEquals(App.getHello(), "Hello"); 
  }
}
