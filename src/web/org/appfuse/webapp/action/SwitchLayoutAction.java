package org.appfuse.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * Implementation of <strong>Action</strong> that switches to different
 * layouts based on certain conditions:
 *
 * <ul>
 * <li>condition 1: if "print=true" passed in, used printLayout</li>
 * </ul>
 *
 * <p>
 * <a href="SwitchLayoutAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Matt Raible
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:15 $
 */
public final class SwitchLayoutAction extends Action {
    //~ Instance fields ========================================================

    /** The <code>Log</code> instance for this class */
    private Log log = LogFactory.getLog(SwitchLayoutAction.class);

    //~ Methods ================================================================

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {
        boolean print =
            Boolean.valueOf(request.getParameter("print")).booleanValue();

        // see if a print parameter is passed in the request
        if (print) {
            log.debug("switching base layout to printing...");

            return mapping.findForward("printLayout");
        } else {
            return mapping.findForward("baseLayout");
        }
    }
}
