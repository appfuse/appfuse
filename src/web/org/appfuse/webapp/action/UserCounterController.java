package org.appfuse.webapp.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;
import org.appfuse.webapp.listener.UserCounterListener;


/**
 * UserCounterController class.  This class is used to display the number of
 * current users on a particular tile (JSP page).
 * 
 * <p>
 * <a href="UserCounterController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2004/05/16 02:16:59 $
 */
public final class UserCounterController extends ControllerSupport {
    //~ Methods ================================================================
    /**
     * This method illustrates a simple example of using a Tiles Controller
     * to get a "current users" counter for this application.
     *
     * @param tilesContext Current tile context
     * @param request Current request
     * @param response Current response
     * @param servletContext Current Servlet Context
     */
    public void execute(ComponentContext tilesContext,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        ServletContext servletContext)
      throws Exception {
        // Get the number of current users from the application's context
        String userCounter =
            (String) servletContext.getAttribute(UserCounterListener.COUNT_KEY);

        // Add this number to the request for display
        request.setAttribute(UserCounterListener.COUNT_KEY, userCounter);
    }
}
